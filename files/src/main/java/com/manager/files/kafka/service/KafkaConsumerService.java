package com.manager.files.kafka.service;

import jakarta.annotation.PostConstruct;

import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.manager.files.service.FileAnalysisService;

@Service
public class KafkaConsumerService {

    private final FileAnalysisService fileAnalysisService;

    public KafkaConsumerService(FileAnalysisService fileAnalysisService) {
        this.fileAnalysisService = fileAnalysisService;
    }

    @PostConstruct
    public void init() {
        System.out.println("🔥🔥🔥 CONSUMER BEAN CREATED 🔥🔥🔥");
    }

    @KafkaListener(
        topics = "file-process",
        groupId = "file-processing-group"
    )
    public void processFile(String fileId) 
        throws
        JobExecutionAlreadyRunningException,
        JobInstanceAlreadyCompleteException,
        JobRestartException,
        InvalidJobParametersException
    {

        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + fileId);
        System.out.println("=================================");

        fileAnalysisService.processFile(fileId);

    }
}