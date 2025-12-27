package com.codesprint.platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubmitRequest {
    @NotBlank
    private String language;

    @NotBlank
    private String code;

    private String competitionId;
}