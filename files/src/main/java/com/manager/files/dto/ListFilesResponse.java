package com.manager.files.dto;

import java.util.Map;

import com.manager.files.analysis.model.FileMetaData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListFilesResponse {
    private String id;
    private String filename;
    private Long size;

    private Map<String, Object> metadata;

    private Map<String, FileMetaData> columnAnalysis;
}
