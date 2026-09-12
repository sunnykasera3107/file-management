package com.gateway.api.controller;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.api.annotation.ApiController;
import com.gateway.api.dto.file.UploadRequest;
import com.gateway.api.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@ApiController 
@Tag(
    name = "File management", 
    description = "This API for file management and analysis."
)
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    @Operation(
        summary = "Upload file",
        description = "Upload file server and attache it to user account.",
        security = {
            @SecurityRequirement(name = "csrfToken")
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "File already exist"),
            @ApiResponse(responseCode = "403", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
        }
    )
    @PostMapping(
        value = "",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadFile(
        @RequestBody UploadRequest file,
        HttpServletRequest httpRequest
    ) throws Exception {
        if (file.getFile().isEmpty()){
            throw new FileNotFoundException("No file selected to upload");
        }

        if (!file.getFile().getOriginalFilename().toLowerCase().endsWith(".xlsx")){
            throw new FileUploadException("Only xlsx files are allowed");
        }
        String token = (String) httpRequest.getAttribute("access_token");
        Map<String, String> response = fileService.uploadFile(file, token);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(
        summary = "List files",
        description = "List files for current user."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "List of files"),
            @ApiResponse(responseCode = "404", description = "File does not exist"),
            @ApiResponse(responseCode = "500", description = "Server error")
        }
    )
    @GetMapping("/list")
    public ResponseEntity<?> listFiles(
        HttpServletRequest httpRequest
    ) throws Exception {
        String token = (String) httpRequest.getAttribute("access_token");
        List<?> response = fileService.listFiles(token);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(
        summary = "Process file",
        description = "Process file to analyse.",
        security = {
            @SecurityRequirement(name = "csrfToken")
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "File analysis process started"),
            @ApiResponse(responseCode = "404", description = "File does not exist"),
            @ApiResponse(responseCode = "403", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
        }
    )
    @PostMapping("/process")
    public ResponseEntity<?> processFile(
        @RequestParam("fileId") String fileId,
        HttpServletRequest httpRequest
    ) throws Exception {
        String token = (String) httpRequest.getAttribute("access_token");
        Map<String, Long> response = fileService.processFile(fileId, token);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(
        summary = "Get process status",
        description = "Get process status with process id."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "File status fetched successfully"),
            @ApiResponse(responseCode = "404", description = "File does not exist"),
            @ApiResponse(responseCode = "403", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
        }
    )
    @GetMapping("/process/{processId}/status")
    public ResponseEntity<?> getProcessStatus(
        @RequestParam("processId") Long jobId,
        HttpServletRequest httpRequest
    ) throws Exception {
        String token = (String) httpRequest.getAttribute("access_token");
        Map<String, String> response = fileService.getProcessStatus(jobId, token);
        return ResponseEntity.ok()
                .body(response);
    }


    @Operation(
        summary = "Restart file process",
        description = "Restart file process analysis with process id.",
        security = {
            @SecurityRequirement(name = "csrfToken")
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "File process restarted successfully"),
            @ApiResponse(responseCode = "404", description = "File does not exist"),
            @ApiResponse(responseCode = "403", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
        }
    )
    @PostMapping("/process/restart")
    public ResponseEntity<?> restartProcessStatus(
        @RequestParam("processId") Long jobId,
        HttpServletRequest httpRequest
    ) throws Exception {
        String token = (String) httpRequest.getAttribute("access_token");
        Map<String, String> response = fileService.restartProcess(jobId, token);
        return ResponseEntity.ok()
                .body(response);
    }


}
