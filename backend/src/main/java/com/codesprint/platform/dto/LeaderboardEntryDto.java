package com.codesprint.platform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaderboardEntryDto {
    private String userId;
    private Long score;
    private Long totalTimeMs;
    private Integer rank;
}
