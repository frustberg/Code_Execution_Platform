package com.codesprint.platform.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SubmissionResultDto {
    private String status;
    private Long totalTimeMs;
    private java.util.List<TestResult> results;
    private String message;

    @Data
    @Builder
    public static class TestResult {
        private String testId;
        private boolean passed;
        private Long timeMs;
        private String input;
        private String expected;
        private String output;
        private String error;
    }
}