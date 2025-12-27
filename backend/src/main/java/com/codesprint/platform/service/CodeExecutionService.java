package com.codesprint.platform.service;

import com.codesprint.platform.dto.SubmissionResultDto;
import com.codesprint.platform.model.Problem;
import com.codesprint.platform.model.Submission;
import com.codesprint.platform.repo.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CodeExecutionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionResultDto runAndPersist(Problem problem, String userId, String competitionId,
                                             String language, String code) {

        List<SubmissionResultDto.TestResult> testResults = new ArrayList<>();
        long totalTime = 0L;
        boolean allPassed = true;

        if (problem.getTestCases() == null || problem.getTestCases().isEmpty()) {
            Submission submission = Submission.builder()
                    .problemId(problem.getId())
                    .userId(userId)
                    .language(language)
                    .code(code)
                    .status("NO_TESTS")
                    .competitionId(competitionId)
                    .totalTimeMs(0L)
                    .createdAt(Instant.now())
                    .build();
            submissionRepository.save(submission);

            return SubmissionResultDto.builder()
                    .status("NO_TESTS")
                    .totalTimeMs(0L)
                    .results(List.of())
                    .message("No test cases configured for this problem.")
                    .build();
        }

        if (!"python".equalsIgnoreCase(language)) {
            Submission submission = Submission.builder()
                    .problemId(problem.getId())
                    .userId(userId)
                    .language(language)
                    .code(code)
                    .status("LANGUAGE_NOT_SUPPORTED")
                    .competitionId(competitionId)
                    .totalTimeMs(0L)
                    .createdAt(Instant.now())
                    .build();
            submissionRepository.save(submission);

            return SubmissionResultDto.builder()
                    .status("LANGUAGE_NOT_SUPPORTED")
                    .totalTimeMs(0L)
                    .results(List.of())
                    .message("Currently only Python execution is supported on the backend.")
                    .build();
        }

        int idx = 1;
        long timeoutSeconds = problem.getTimeoutSeconds() != null ? problem.getTimeoutSeconds() : 5L;
        for (Problem.TestCase tc : problem.getTestCases()) {
            long start = System.nanoTime();
            ExecResult exec = runPython(code, tc.getInput(), timeoutSeconds);
            long end = System.nanoTime();
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(end - start);
            totalTime += elapsedMs;

            boolean passed = !exec.timedOut && exec.exitCode == 0 &&
                    (exec.stdout.trim().equals(tc.getExpectedOutput().trim()));

            if (!passed) {
                allPassed = false;
            }

            SubmissionResultDto.TestResult tr = SubmissionResultDto.TestResult.builder()
                    .testId(String.valueOf(idx))
                    .passed(passed)
                    .timeMs(elapsedMs)
                    .input(tc.getInput())
                    .expected(tc.getExpectedOutput())
                    .output(exec.stdout)
                    .error(exec.stderr)
                    .build();
            testResults.add(tr);
            idx++;
        }

        String finalStatus = allPassed ? "SUCCESS" : "FAILED";

        Submission submission = Submission.builder()
                .problemId(problem.getId())
                .userId(userId)
                .language(language)
                .code(code)
                .status(finalStatus)
                .competitionId(competitionId)
                .totalTimeMs(totalTime)
                .createdAt(Instant.now())
                .build();
        submissionRepository.save(submission);

        return SubmissionResultDto.builder()
                .status(finalStatus)
                .totalTimeMs(totalTime)
                .results(testResults)
                .message(allPassed ? "All test cases passed." : "One or more test cases failed.")
                .build();
    }

    private ExecResult runPython(String code, String input, long timeoutSeconds) {
        ExecResult result = new ExecResult();
        try {
            Path tempDir = Files.createTempDirectory("codeexec_py_");
            Path script = tempDir.resolve("main.py");
            Files.writeString(script, code, StandardCharsets.UTF_8);

            ProcessBuilder pb = new ProcessBuilder("python", script.toAbsolutePath().toString());
            pb.directory(tempDir.toFile());
            pb.redirectErrorStream(false);
            Process process = pb.start();

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                if (input != null && !input.isEmpty()) {
                    writer.write(input);
                }
                writer.flush();
            }

            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                result.timedOut = true;
                result.exitCode = -1;
                result.stdout = "";
                result.stderr = "⏱️ TIMEOUT: Code exceeded " + timeoutSeconds + " seconds";
                return result;
            }

            result.exitCode = process.exitValue();
            result.stdout = readStream(process.getInputStream());
            result.stderr = readStream(process.getErrorStream());

        } catch (Exception e) {
            result.exitCode = -1;
            result.stdout = "";
            result.stderr = "Execution error: " + e.getMessage();
        }
        return result;
    }

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (!first) {
                    sb.append(System.lineSeparator());
                }
                sb.append(line);
                first = false;
            }
            return sb.toString();
        }
    }

    private static class ExecResult {
        int exitCode;
        String stdout;
        String stderr;
        boolean timedOut;
    }
}