package com.manager.log.kafka.service;

import jakarta.annotation.PostConstruct;

import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobRestartException;
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
        this.logRepository = logRepository;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("🔥🔥🔥 CONSUMER BEAN CREATED 🔥🔥🔥");
    }

    @KafkaListener(topics = "file-exception")
    public void logFileService(LogProducer log) 
        throws
        JobExecutionAlreadyRunningException,
        JobInstanceAlreadyCompleteException,
        JobRestartException,
        InvalidJobParametersException
    {

        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("=================================");
        logRepository.save(log);
    }

    @KafkaListener(topics = "user-exception")
    public void logUserService(LogProducer log) 
        throws
        JobExecutionAlreadyRunningException,
        JobInstanceAlreadyCompleteException,
        JobRestartException,
        InvalidJobParametersException
    {

        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("=================================");
        logRepository.save(log);

    }

    @KafkaListener(topics = "api-exception")
    public void logAPIService(LogProducer log) 
        throws
        JobExecutionAlreadyRunningException,
        JobInstanceAlreadyCompleteException,
        JobRestartException,
        InvalidJobParametersException
    {

        System.out.println("=================================");
        System.out.println("🔥 KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + log.getMessage());
        System.out.println("=================================");
        logRepository.save(log);

    }
}