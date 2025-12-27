package com.codesprint.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "problems")
public class Problem {

    @Id
    private String id;

    private String title;
    private String statement;
    private String level;
    private List<String> tags;
    private Map<String, String> codeStubs;
    private List<TestCase> testCases;
    private Long timeoutSeconds;  // Added timeout field

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestCase {
        private String input;
        private String expectedOutput;
        private boolean hidden;
    }
}