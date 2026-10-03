package com.gateway.api.kafka.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogProducer {
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
