package com.gateway.api.dto.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class GeneralExceptionResponse {
    private Integer status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public  GeneralExceptionResponse(Integer status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
