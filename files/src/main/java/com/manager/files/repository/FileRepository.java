package com.manager.files.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.manager.files.model.FileDocument;

@Repository
public interface FileRepository extends MongoRepository<FileDocument, ObjectId> {
    FileDocument findByFilename(String filename);
    List<FileDocument> findByUserId(String id);
}
