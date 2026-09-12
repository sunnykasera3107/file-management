package com.manager.files.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.manager.files.analysis.model.FileMetaData;

import lombok.Data;

@Data
@Document(collection = "fileDocument")
public class FileDocument {
    @Id
    private String id;

    private String filename;
    private String filePath;
    private String userId;
    private Long size;

    private Map<String, Object> metadata;

    private Map<String, FileMetaData> columnAnalysis;
    
}
