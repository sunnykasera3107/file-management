package com.manager.files.service;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Service;

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

    public Map<String, Long> processFile(String fileId) 
        throws Exception
    {
        System.out.println("File analysis start");

        FileDocument file = fileRepository
            .findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found"));

        if (file != null) {
            String path = file.getFilePath();

            JobParameters jobParameter = 
                new JobParametersBuilder()
                    .addString(
                        "filePath",
                        path
                    )
                    .addString(
                        "fileId",
                        file.getId().toString()
                    )
                    .toJobParameters();
            
            JobExecution newJob = asyncJobOperator.start(
                fileAnalysisJob, 
                jobParameter
            );

            Map<String, Object> metaData = file.getMetadata();

            metaData.put("jobId", newJob.getId());
            
            file.setMetadata(metaData);
            
            fileRepository.save(file);
            
            return Map.of("jobId", newJob.getId());
                        
        }
        
        System.out.println("File analysis end");
        return null;
    }

    public Map<String, String> getJobStatus(long id) {
        JobExecution jobExecution = jobRepository.getJobExecution(id);
        String status = jobExecution.getStatus().toString();
        return Map.of("status", status);
    }

    public Map<String, String> restartJob(long id) 
        throws JobRestartException
    {
        System.out.println("Job Id");
        System.out.println(id);
        JobExecution jobExecution = jobRepository.getJobExecution(id);
        System.out.println(jobExecution);
        asyncJobOperator.restart(jobExecution);
        System.out.println("test");
        return Map.of("message", "Job restarted");
    } 
}
