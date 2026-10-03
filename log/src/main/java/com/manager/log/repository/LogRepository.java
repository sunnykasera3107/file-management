package com.manager.log.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.manager.log.model.LogDocument;

@Repository
public interface LogRepository extends MongoRepository<LogDocument, String> {
}
