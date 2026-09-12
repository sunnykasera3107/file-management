package com.manager.files.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manager.files.dto.ListFilesResponse;
import com.manager.files.dto.ProcessFileRequest;
import com.manager.files.dto.ProcessRestartRequest;
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
        @RequestBody MultipartFile file,
        @AuthenticationPrincipal Jwt jwt
    ) throws IOException {

        if (!file.isEmpty()) {
            return fileService.uploadFile(file, jwt.getSubject());
        }

        return Map.of("message", "No file to upload");
    }

    @GetMapping
    public List<ListFilesResponse> getFiles(
        @AuthenticationPrincipal Jwt jwt
    ) {
       return fileService.getFiles(jwt.getSubject());
    }

    @PostMapping("/process")
    public Map<String, Long> processFile(
        @RequestBody ProcessFileRequest request
    ) throws Exception {
        return fileAnalysisService.processFile(request.getFileId());
    }

    @GetMapping("/process/{id}/status")
    public Map<String, String> getFileStatus(
        @PathVariable("id") long id
    ) throws Exception {
        return fileAnalysisService.getJobStatus(id);
    }

    @PostMapping("/process/restart")
    public Map<String, String> restartJob(
        @RequestBody ProcessRestartRequest request
    ) throws Exception {
        System.out.println(request.getId());
        Map<String, String> response = fileAnalysisService.restartJob(request.getId());
        return response;
    }
}
