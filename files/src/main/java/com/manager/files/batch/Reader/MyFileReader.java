package com.manager.files.batch.Reader;

import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.manager.files.analysis.service.ReaderService;

@Component
@StepScope
public class MyFileReader implements ItemReader<Row>{

    private final ReaderService readerService;

    private final String filePath;

    private Iterator<Row> rows;

    public MyFileReader(
        ReaderService readerService, 
         @Value("#{jobParameters['filePath']}")  String filePath
    ) throws IOException {
        this.readerService = readerService;
        this.filePath = filePath;
        rows = this.readerService.readFileStream(this.filePath);
    }
    
    @Override
    public Row read() {

        if (rows.hasNext()) {
            return rows.next();
        }
        return null;
    }
}
