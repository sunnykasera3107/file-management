package com.manager.files.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.manager.files.model.FileDocument;
import com.manager.files.repository.FileRepository;

@Service
public class FileService {

    
    @Value("${file.storage.path}")
    private String targetPath;
    
    @Value("${file.read.bytesize}")
    private Integer byteSize;

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    
    public Map<String, String> uploadFile( 
        MultipartFile file,
        String userId
    ) throws IOException {

        Path directory = Path.of(targetPath, userId);
        System.out.println(directory);

        if(!Files.isDirectory(directory)){
            Files.createDirectories(directory);
        }

        Path target = directory.resolve(file.getOriginalFilename());

        ArrayList<Integer> hashCodes = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
            OutputStream outputStream = Files.newOutputStream(target)) {

            byte[] buffer = new byte[byteSize];

            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] chunkData = Arrays.copyOf(buffer, bytesRead);
                hashCodes.add(Arrays.hashCode(chunkData));
                outputStream.write(buffer, 0, bytesRead);
            }

            int fileHash = Arrays.hashCode(hashCodes.toArray());

            boolean saved = saveFileInfo(
                file.getOriginalFilename(), 
                target.toString(),
                file.getSize(), 
                userId,
                fileHash
            );

            if (saved) {
                return Map.of("message", "File uploaded successfully.");
            }
        }
        return Map.of("message", "File not uploaded.");
    }

    public boolean saveFileInfo(
        String filename,
        String filepath,
        Long size,
        String userId,
        int fileHash
    ) {
        FileDocument existingFile = fileRepository
                .findByFilename(filename);

        if (existingFile != null) {
            if (!existingFile.getMetadata().isEmpty()){
                int existingFileHash = (Integer) existingFile.getMetadata().get("hash");
                if (existingFileHash == fileHash) {
                    System.out.println("File already exist");
                    return false;
                }
            }
        }
        
        Map<String, Object> metaData = new TreeMap<>();
        metaData.put("hash", fileHash);

        FileDocument file = new FileDocument();
        file.setFilename(filename);
        file.setFilePath(filepath);
        file.setSize(size);
        file.setUserId(userId);
        file.setMetadata(metaData);
        FileDocument savedFile = fileRepository.save(file);
        if (savedFile != null) {
            System.out.println(savedFile.toString());
            return true;
        }
        return false;
    }

    public List<FileDocument> getFiles(String userId) {
        return fileRepository.findByUserId(userId);
    }
}
