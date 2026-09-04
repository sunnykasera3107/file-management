package com.manager.files.batch.Processor;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.manager.files.analysis.model.FileMetaData;
import com.manager.files.analysis.service.ProcessorService;

@Component
@StepScope
public class MyFileProcessor implements ItemProcessor<Row, Map<String, FileMetaData>> {

    private final ProcessorService processorService;

    public MyFileProcessor(ProcessorService processorService) {
        this.processorService = processorService;
    }
    
    @Override 
    public Map<String, FileMetaData> process(Row item) {
        Map<String, FileMetaData> analysis = processorService.analyse(item);
        return analysis;
    }
}
