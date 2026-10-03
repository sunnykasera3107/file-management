package com.manager.files.exception;

import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.manager.files.dto.GeneralExceptionResponse;
import com.manager.files.kafka.dto.LogProducer;
import com.manager.files.kafka.service.KafkaService;
    
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final KafkaService kafkaService;

    public GlobalExceptionHandler(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
    public ResponseEntity<GeneralExceptionResponse> handleJobAlreadyComplete(
            JobInstanceAlreadyCompleteException ex) {

        LogProducer log = new LogProducer();
        log.setLevel("INFO");
        log.setEventType("COMPLETED_JOB_EXECUTION_RETRY");
        log.setMessage("Already completed job execution retry to execute.");
        log.setExceptionType("JobInstanceAlreadyCompleteException");
        log.setExceptionMessage(ex.getMessage());
        log.setTraceMessage(ex.getStackTrace().toString());
        log.setHttpStatus(409);

        kafkaService.send(log);
        
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

        LogProducer log = new LogProducer();
        log.setLevel("WARN");
        log.setEventType("JOB_EXECUTION_RESTART");
        log.setMessage("Job execution cannot be restarted.");
        log.setExceptionType("JobRestartException");
        log.setExceptionMessage(ex.getMessage());
        log.setTraceMessage(ex.getStackTrace().toString());
        log.setHttpStatus(409);

        kafkaService.send(log);
        
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
                System.out.println(ex.getMessage());
        
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
                    "Internal server error",
                    "Something went wrong"
                )
            );
    }

    @ExceptionHandler(FileAlreadyExistException.class)
    public ResponseEntity<GeneralExceptionResponse> handleFileAlreadyExistException(
            Exception ex) {

        LogProducer log = new LogProducer();
        log.setLevel("INFO");
        log.setEventType("FILE_ALREADY_EXIST");
        log.setMessage("File already exists");
        log.setExceptionType("FileAlreadyExistException");
        log.setExceptionMessage(ex.getMessage());
        log.setTraceMessage(ex.getStackTrace().toString());
        log.setHttpStatus(409);

        kafkaService.send(log);

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
