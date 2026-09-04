package com.manager.files.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manager.files.model.FileDocument;
import com.manager.files.service.FileAnalysisService;
import com.manager.files.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    private final FileAnalysisService fileAnalysisService;

    public FileController(
        FileService fileService,
        FileAnalysisService fileAnalysisService
    ) {
        this.fileService = fileService;
        this.fileAnalysisService = fileAnalysisService;
    }
    
    @PostMapping
    public Map<String, String> uploadFile(
        @RequestParam("file") MultipartFile file,
        @AuthenticationPrincipal Jwt jwt
    ) throws IOException {
        
        if (!file.isEmpty()) {
            return fileService.uploadFile(file, jwt.getSubject());
        }

        return Map.of("message", "No file to upload");
    }

    @GetMapping
    public List<FileDocument> getFiles(
        @AuthenticationPrincipal Jwt jwt
    ) {
       return fileService.getFiles(jwt.getSubject());
    }

    @PostMapping("/process")
    public void processFile(
        @RequestParam("fileId") String fileId
    ) throws Exception {
        fileAnalysisService.processFile(fileId);
    }

    @GetMapping("/process/{id}/status")
    public String getFileStatus(
        @PathVariable("id") long id
    ) throws Exception {
        return fileAnalysisService.getJobStatus(id);
    }

    @PostMapping("/process/restart")
    public void restartJob(
        @RequestParam("id") long id
    ) throws Exception {
        fileAnalysisService.restartJob(id);
    }
}
