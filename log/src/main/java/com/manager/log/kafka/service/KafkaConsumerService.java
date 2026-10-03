package com.manager.log.kafka.service;

import jakarta.annotation.PostConstruct;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.manager.log.model.LogDocument;
import com.manager.log.repository.LogRepository;

@Service
public class KafkaConsumerService {

    private final LogRepository logRepository;

    public KafkaConsumerService(
        LogRepository logRepository
    ) {
        System.out.println("🔥🔥🔥 CONSTRUCTOR CALLED 🔥🔥🔥");
        this.logRepository = logRepository;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("🔥🔥🔥 CONSUMER BEAN CREATED 🔥🔥🔥");
    }

    @KafkaListener(
        topics = "file-exception",
        groupId = "log-processing-group"
    )
    public void logFileService(LogDocument log) {

        System.out.println("==================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("=================================");
        logRepository.save(log);
    }

    @KafkaListener(
        topics = "user-exception",
        groupId = "log-processing-group"
    )
    public void logUserService(LogDocument log) {

        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("=================================");
        logRepository.save(log);

    }

    @KafkaListener(
        topics = "api-exception",
        groupId = "log-processing-group"
    )
    public void logAPIService(LogDocument log) {

        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("==================================");
        logRepository.save(log);

    }
}