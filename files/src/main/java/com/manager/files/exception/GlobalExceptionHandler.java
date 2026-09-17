package com.manager.files.exception;

import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.manager.files.dto.GeneralExceptionResponse;
    
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
    public ResponseEntity<GeneralExceptionResponse> handleJobAlreadyComplete(
            JobInstanceAlreadyCompleteException ex) {
        return ResponseEntity
            .status(409)
            .body(
                new GeneralExceptionResponse(
                    409,
                    "Job already completed",
                    ex.getMessage()
                )
            );
    }

    @ExceptionHandler(JobRestartException.class)
    public ResponseEntity<GeneralExceptionResponse> handleJobRestart(
            JobRestartException ex) {
        
       return ResponseEntity
            .status(409)
            .body(
                new GeneralExceptionResponse(
                    409,
                    "Job cannot be restarted",
                    ex.getMessage()
                )
            );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralExceptionResponse> handleGenericException(
            Exception ex) {
        return ResponseEntity
            .status(500)
            .body(
                new GeneralExceptionResponse(
                    500,
                    "Internal server error",
                    ex.getMessage()
                )
            );
    }

    @ExceptionHandler(FileAlreadyExistException.class)
    public ResponseEntity<GeneralExceptionResponse> handleFileAlreadyExistException(
            Exception ex) {

        return ResponseEntity
            .status(409)
            .body(
                new GeneralExceptionResponse(
                    409,
                    "File already exist",
                    ex.getMessage()
                )
            );
    }

}
