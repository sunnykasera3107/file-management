package com.manager.files.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manager.files.dto.FilesResponse;
import com.manager.files.dto.GeneralResponse;
import com.manager.files.dto.ProcessFileRequest;
import com.manager.files.dto.ProcessRestartRequest;
import com.manager.files.model.FileDocument;
import com.manager.files.service.FileAnalysisService;
import com.manager.files.service.FileService;
import com.manager.files.service.KafkaService;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    private final FileAnalysisService fileAnalysisService;

    private final KafkaService kafkaService;

    public FileController(
        FileService fileService,
        FileAnalysisService fileAnalysisService,
        KafkaService kafkaService
    ) {
        this.fileService = fileService;
        this.fileAnalysisService = fileAnalysisService;
        this.kafkaService = kafkaService;
    }
    
    @PostMapping
    public GeneralResponse uploadFile(
        @RequestBody MultipartFile file,
        @AuthenticationPrincipal Jwt jwt
    ) throws IOException {

        if (!file.isEmpty()) {
            return fileService.uploadFile(file, jwt.getSubject());
        }

        throw new FileNotFoundException("No file selected to upload");
    }

    @GetMapping
    public List<FilesResponse> getFiles(
        @AuthenticationPrincipal Jwt jwt
    ) {
       return fileService.getFiles(jwt.getSubject());
    }

    @GetMapping("/{id}")
    public FilesResponse getFiles(
        @PathVariable("id") String id,
        @AuthenticationPrincipal Jwt jwt
    ) {
       FilesResponse file = fileService.getFile(id);
       return file;
    }

    @PostMapping("/process")
    public FilesResponse processFile(
        @RequestBody ProcessFileRequest request
    ) throws Exception {
        kafkaService.send(request.getFileId());
        return fileService.getFile(request.getFileId());
    }

    @GetMapping("/process/{id}/status")
    public GeneralResponse getFileStatus(
        @PathVariable("id") long id
    ) throws Exception {
        return fileAnalysisService.getJobStatus(id);
    }

    @PostMapping("/process/restart")
    public GeneralResponse restartJob(
        @RequestBody ProcessRestartRequest request
    ) throws Exception {
        return fileAnalysisService.restartJob(request.getId());
    }
}
