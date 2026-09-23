package com.manager.files.service;

import java.util.Map;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.manager.files.dto.GeneralResponse;
import com.manager.files.dto.ProcessFileResponse;
import com.manager.files.kafka.topics.KafkaTopics;
import com.manager.files.model.FileDocument;
import com.manager.files.repository.FileRepository;

@Service
public class FileAnalysisService {

    private final FileRepository fileRepository;
    
    private final TaskExecutorJobOperator asyncJobOperator;

    private final Job fileAnalysisJob;

    private final JobRepository jobRepository;

    public FileAnalysisService(
        FileRepository fileRepository,
        TaskExecutorJobOperator jobOperator, 
        Job fileAnalysisJob,
        JobRepository jobRepository
    ) {
        this.asyncJobOperator = jobOperator;
        this.fileAnalysisJob = fileAnalysisJob;
        this.fileRepository = fileRepository;
        this.jobRepository = jobRepository;
    }

    @KafkaListener(
        topics = "file-process",
        groupId = "file-processing-group"
    )
     public void processFile(String fileId) {

        System.out.println("=================================");
        System.out.println("KAFKA MESSAGE RECEIVED");
        System.out.println("File ID: " + fileId);
        System.out.println("=================================");
    }

    public void processFile1(
        String fileId
    ) throws 
        JobExecutionAlreadyRunningException,
        JobInstanceAlreadyCompleteException,
        JobRestartException,
        InvalidJobParametersException
    {
        System.out.println("kafka listened");
        System.out.println(fileId);
        // FileDocument file = fileRepository
        //     .findById(fileId)
        //     .orElseThrow(() -> new RuntimeException("File not found"));

        // if (file != null) {
        //     String path = file.getFilePath();

        //     JobParameters jobParameter = 
        //         new JobParametersBuilder()
        //             .addString(
        //                 "filePath",
        //                 path
        //             )
        //             .addString(
        //                 "fileId",
        //                 file.getId().toString()
        //             )
        //             .toJobParameters();
            
        //     JobExecution newJob = asyncJobOperator.start(
        //         fileAnalysisJob, 
        //         jobParameter
        //     );

        //     Map<String, Object> metaData = file.getMetadata();

        //     metaData.put("jobId", newJob.getId());
            
        //     file.setMetadata(metaData);
            
        //     fileRepository.save(file);
            
        //     return new ProcessFileResponse(newJob.getId());
                        
        // }
    }

    public GeneralResponse getJobStatus(long id) {
        JobExecution jobExecution = jobRepository.getJobExecution(id);
        String status = jobExecution.getStatus().toString();
        return new GeneralResponse(status);
    }

    public GeneralResponse restartJob(long id) 
        throws JobRestartException
    {
        System.out.println("Job Id");
        System.out.println(id);
        JobExecution jobExecution = jobRepository.getJobExecution(id);
        System.out.println(jobExecution);
        asyncJobOperator.restart(jobExecution);
        System.out.println("test");
        return new GeneralResponse("Job restarted");
    } 
}
