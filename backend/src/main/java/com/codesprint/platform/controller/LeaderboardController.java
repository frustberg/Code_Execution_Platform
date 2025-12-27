package com.codesprint.platform.controller;

import com.codesprint.platform.dto.LeaderboardEntryDto;
import com.codesprint.platform.model.Submission;
import com.codesprint.platform.repo.SubmissionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = "http://localhost:5173")
public class LeaderboardController {

    private final SubmissionRepository submissionRepository;

    public LeaderboardController(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @GetMapping("/{competitionId}")
    public List<LeaderboardEntryDto> getLeaderboard(@PathVariable String competitionId,
                                                    @RequestParam(defaultValue = "100") int limit) {
        List<Submission> subs = (competitionId == null || competitionId.isBlank())
                ? submissionRepository.findAll()
                : submissionRepository.findByCompetitionId(competitionId);

        Map<String, List<Submission>> byUser = subs.stream()
                .collect(Collectors.groupingBy(Submission::getUserId));

        List<LeaderboardEntryDto> entries = byUser.entrySet().stream().map(e -> {
            String userId = e.getKey();
            List<Submission> userSubs = e.getValue();
            long score = userSubs.stream()
                    .filter(s -> "SUCCESS".equalsIgnoreCase(s.getStatus()))
                    .count();
            long totalTime = userSubs.stream()
                    .map(Submission::getTotalTimeMs)
                    .filter(Objects::nonNull)
                    .mapToLong(Long::longValue)
                    .sum();
            return LeaderboardEntryDto.builder()
                    .userId(userId)
                    .score(score)
                    .totalTimeMs(totalTime)
                    .build();
        }).sorted(Comparator.comparing(LeaderboardEntryDto::getScore).reversed()
                .thenComparing(LeaderboardEntryDto::getTotalTimeMs))
                .limit(limit)
                .collect(Collectors.toList());

        int rank = 1;
        for (LeaderboardEntryDto e : entries) {
            e.setRank(rank++);
        }
        return entries;
    }
}