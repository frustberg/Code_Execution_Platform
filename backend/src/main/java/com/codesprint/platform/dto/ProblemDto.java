package com.codesprint.platform.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ProblemDto {
    private String id;
    private String title;
    private String statement;
    private String level;
    private List<String> tags;
    private Map<String, String> codeStubs;
}