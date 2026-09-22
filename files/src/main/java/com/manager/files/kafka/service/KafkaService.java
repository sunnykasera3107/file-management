package com.manager.files.kafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.manager.files.kafka.topics.KafkaTopics;

@Service 
public class KafkaService {
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String fileId) {
        System.out.println(fileId);
        kafkaTemplate.send(
            KafkaTopics.FILE_PROCESSING, 
            fileId
        );
    }
}
