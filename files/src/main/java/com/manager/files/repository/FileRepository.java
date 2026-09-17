package com.manager.files.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.manager.files.dto.FilesResponse;
import com.manager.files.model.FileDocument;

@Repository
public interface FileRepository extends MongoRepository<FileDocument, String> {
    FilesResponse findByFilename(String filename);
    List<FilesResponse> findByUserId(String id);
}
