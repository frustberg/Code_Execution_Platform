package com.codesprint.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "submissions")
public class Submission {

    @Id
    private String id;

    private String problemId;
    private String userId;
    private String language;
    private String code;
    private String status;
    private Long totalTimeMs;
    private String competitionId;
    private Instant createdAt;
}