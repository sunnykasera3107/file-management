package com.manager.files.kafka.service;

import jakarta.annotation.PostConstruct;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @PostConstruct
    public void init() {
        System.out.println("🔥🔥🔥 CONSUMER BEAN CREATED 🔥🔥🔥");
    }

    @KafkaListener(
        topics = "file-process",
        groupId = "file-processing-group"
    )
    public void processFile(String fileId) {

        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + fileId);
        System.out.println("=================================");
    }
}