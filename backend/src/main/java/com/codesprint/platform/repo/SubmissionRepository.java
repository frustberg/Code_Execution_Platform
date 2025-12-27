package com.codesprint.platform.repo;

import com.codesprint.platform.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<Submission> findByCompetitionId(String competitionId);
}