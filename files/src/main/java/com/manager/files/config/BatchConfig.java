package com.manager.files.config;

import java.util.Map;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.job.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.manager.files.analysis.model.FileMetaData;
import com.manager.files.batch.Processor.MyFileProcessor;
import com.manager.files.batch.Reader.MyFileReader;
import com.manager.files.batch.Writer.MyFileWriter;

import jakarta.annotation.PostConstruct;

@Configuration 
public class BatchConfig {

    @Bean
    public PlatformTransactionManager transactionManager(
            DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Job fileAnalysisJob(
            JobRepository jobRepository,
            Step fileAnalysisStep) {

        return new JobBuilder("fileAnalysis", jobRepository)
                .start(fileAnalysisStep)
                .build();
    }

    @Bean
    public Step fileAnalysisStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MyFileReader reader,
            MyFileProcessor processor,
            MyFileWriter writer) {

        return new StepBuilder("fileAnalysis", jobRepository)
                .<Row, Map<String, FileMetaData>>chunk(3)
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public TaskExecutorJobOperator asyncJobOperator(
            JobRepository jobRepository,
            JobRegistry jobRegistry,
            TaskExecutor taskExecutor) {

        TaskExecutorJobOperator operator =
                new TaskExecutorJobOperator();

        operator.setJobRepository(jobRepository);
        operator.setJobRegistry(jobRegistry);
        operator.setTaskExecutor(taskExecutor);

        return operator;
    }

    @PostConstruct 
    public void init() {
        // Set the override to a value higher than your required 184,640,367 (e.g., 250 MB)
        IOUtils.setByteArrayMaxOverride(999_000_000);
    }
}
