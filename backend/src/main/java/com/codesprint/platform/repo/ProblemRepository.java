package com.codesprint.platform.repo;

import com.codesprint.platform.model.Problem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProblemRepository extends MongoRepository<Problem, String> {
}