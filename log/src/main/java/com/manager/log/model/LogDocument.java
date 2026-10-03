package com.manager.log.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Data;

@Data
@Document(collection = "logDocument")
public class LogDocument {
    @Id
    private String id;

    private LocalDateTime timestamp;
    private String serviceName;
    private String environment;
    private String level;
    private String eventType;
    private String message;
    private String traceMessage;
    private String exceptionType;
    private String exceptionMessage;
    private long httpStatus;
    private String objectId;
    private Map<String, Object> metadata;
    
}
