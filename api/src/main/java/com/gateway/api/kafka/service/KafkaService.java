package com.gateway.api.kafka.service;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.gateway.api.kafka.dto.LogProducer;

@Service 
public class KafkaService {
    
    private final KafkaTemplate<String, LogProducer> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, LogProducer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(LogProducer log) {
        log.setTimestamp(LocalDateTime.now());
        log.setServiceName("UserService");
        log.setEnvironment("Test");
        
        kafkaTemplate.send(
            "api-exception",
            log
        ).whenComplete((result, exception) -> {

            if (exception != null) {
                System.out.println("❌ Kafka send failed");
                exception.printStackTrace();
            } else {
                System.out.println("✅ Kafka send successful");
                System.out.println(
                    "Topic: " + result.getRecordMetadata().topic()
                );
                System.out.println(
                    "Partition: " + result.getRecordMetadata().partition()
                );
                System.out.println(
                    "Offset: " + result.getRecordMetadata().offset()
                );
            }
        });
    }
}
