package com.gateway.api.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {

        System.out.println();
         HttpStatusCode status = HttpStatusCode.valueOf(500); 

        if (ex instanceof ResponseStatusException) {
            status = ((ResponseStatusException) ex).getStatusCode();
        }

        Map<String, Object> response = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", status,
            "error", "Internal Server Error",
            "message", ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
    
}
