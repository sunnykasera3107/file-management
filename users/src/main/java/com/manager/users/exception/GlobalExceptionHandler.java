package com.manager.users.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.manager.users.dto.GeneralExceptionResponse;
import com.manager.users.kafka.dto.LogProducer;
import com.manager.users.kafka.service.KafkaService;

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
                    "Something went wrong."
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
                    "User not found with given information name"
                )
            );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<GeneralExceptionResponse> handleUserAlreadyExistsException(
        UserAlreadyExistsException ex
    ) {
        LogProducer log = new LogProducer();
        log.setLevel("INFO");
        log.setEventType("USER_ALREADY_EXIST");
        log.setMessage("User already exist");
        log.setExceptionType("UserAlreadyExistsException");
        log.setExceptionMessage(ex.getMessage());
        log.setTraceMessage(ex.getStackTrace().toString());
        log.setHttpStatus(409);

        kafkaService.send(log);
        
        return ResponseEntity
            .status(409)
            .body(
                new GeneralExceptionResponse(
                    409,
                    "User already exist",
                    "Usre already exist with given email address"
                )
            );
    }
}
