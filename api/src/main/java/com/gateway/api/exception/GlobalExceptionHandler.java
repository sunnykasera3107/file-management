package com.gateway.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import com.gateway.api.dto.exception.GeneralExceptionResponse;
import com.gateway.api.kafka.dto.LogProducer;
import com.gateway.api.kafka.service.KafkaService;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    private final KafkaService kafkaService;

    public GlobalExceptionHandler(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralExceptionResponse> handleException(
        Exception ex
    ) {
        LogProducer log = new LogProducer();
        log.setLevel("ERROR");
        log.setEventType("INTERNAL_ERROR");
        log.setMessage("Internal server error");
        log.setExceptionType("Exception");
        log.setExceptionMessage(ex.getMessage());
        log.setTraceMessage(ex.getStackTrace().toString());
        log.setHttpStatus(500);

        kafkaService.send(log);

        return ResponseEntity
            .status(500)
            .body(
                new GeneralExceptionResponse(
                    500,
                    "Internal Server Error",
                    ex.getMessage()
                )
            );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GeneralExceptionResponse> handleUsernameNotFoundExecption(
        UsernameNotFoundException ex
    ) {
        LogProducer log = new LogProducer();
        log.setLevel("WARN");
        log.setEventType("USERNAME_NOT_FOUND");
        log.setMessage("Username not found");
        log.setExceptionType("UsernameNotFoundException");
        log.setExceptionMessage(ex.getMessage());
        log.setTraceMessage(ex.getStackTrace().toString());
        log.setHttpStatus(404);

        kafkaService.send(log);
        return ResponseEntity
            .status(404)
            .body(
                new GeneralExceptionResponse(
                    404,
                    "User not found",
                    ex.getMessage()
                )
            );
    }

}
