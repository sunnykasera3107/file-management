package com.manager.log.kafka.service;

import jakarta.annotation.PostConstruct;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.manager.log.kafka.dto.LogProducer;
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
    public void logFileService(LogProducer log) {
        System.out.println("==================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("=================================");
        saveLog(log);
    }

    @KafkaListener(
        topics = "user-exception",
        groupId = "log-processing-group"
    )
    public void logUserService(LogProducer log) {
        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("=================================");
        saveLog(log);
    }

    @KafkaListener(
        topics = "api-exception",
        groupId = "log-processing-group"
    )
    public void logAPIService(LogProducer log) {
        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("==================================");
        saveLog(log);
    }

    private void saveLog(LogProducer log) {
        LogDocument logDoc = new LogDocument();
        logDoc.setTimestamp(log.getTimestamp());
        logDoc.setServiceName(log.getServiceName());
        logDoc.setEnvironment(log.getEnvironment());
        logDoc.setLevel(log.getLevel());
        logDoc.setEventType(log.getEventType());
        logDoc.setMessage(log.getMessage());
        logDoc.setTraceMessage(log.getTraceMessage());
        logDoc.setExceptionType(log.getExceptionType());
        logDoc.setExceptionMessage(log.getExceptionMessage());
        logDoc.setHttpStatus(log.getHttpStatus());
        logDoc.setObjectId(log.getObjectId());
        logDoc.setMetadata(log.getMetadata());
        logRepository.save(logDoc);
    }
}