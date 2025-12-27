package com.codesprint.platform.controller;

import com.codesprint.platform.dto.SubmissionResultDto;
import com.codesprint.platform.dto.SubmitRequest;
import com.codesprint.platform.model.Problem;
import com.codesprint.platform.repo.ProblemRepository;
import com.codesprint.platform.service.CodeExecutionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "http://localhost:5173")
public class SubmissionController {

    private final ProblemRepository problemRepository;
    private final CodeExecutionService codeExecutionService;

    public SubmissionController(ProblemRepository problemRepository,
                                CodeExecutionService codeExecutionService) {
        this.problemRepository = problemRepository;
        this.codeExecutionService = codeExecutionService;
    }

    @PostMapping("/problem/{problemId}")
    public SubmissionResultDto submit(@PathVariable String problemId,
                                      @Valid @RequestBody SubmitRequest request,
                                      @RequestHeader(value = "X-User-Id", defaultValue = "demo-user") String userId) {

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        return codeExecutionService.runAndPersist(
                problem,
                userId,
                request.getCompetitionId(),
                request.getLanguage(),
                request.getCode()
        );
    }
}