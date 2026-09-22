package com.manager.files.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class FileProcessingEvent {
    private String fileId;
    private String filename;
    private String filepath;
}
