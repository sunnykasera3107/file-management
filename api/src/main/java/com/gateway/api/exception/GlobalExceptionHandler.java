package com.gateway.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import com.gateway.api.dto.exception.GeneralExceptionResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralExceptionResponse> handleException(
        Exception ex
    ) {
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
