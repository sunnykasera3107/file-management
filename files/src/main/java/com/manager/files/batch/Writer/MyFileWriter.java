package com.manager.files.batch.Writer;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.manager.files.analysis.model.FileMetaData;
import com.manager.files.model.FileDocument;
import com.manager.files.repository.FileRepository;

@Component
@StepScope
public class MyFileWriter implements ItemWriter<Map<String, FileMetaData>>{

    private final FileRepository fileRepository;

    private final String id;

    public MyFileWriter(
        FileRepository fileRepository,
        @Value("#{jobParameters['fileId']}") String id
    ) {
        this.fileRepository = fileRepository;
        this.id = id;
    }
    
    @Override
    public void write(Chunk<? extends Map<String, FileMetaData>> items) 
        throws Exception
    {
        ObjectId fileId = new ObjectId(id);
        FileDocument file = fileRepository
                    .findById(fileId)
                    .orElseThrow(() -> new RuntimeException("File not found with id: " + id));
                            
        items.forEach((item) -> {
            file.setColumnAnalysis(item);
        });

        fileRepository.save(file);
    }
}
