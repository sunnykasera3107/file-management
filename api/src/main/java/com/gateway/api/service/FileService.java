package com.gateway.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.gateway.api.dto.file.UploadRequest;

import jakarta.annotation.PostConstruct;

@Service
public class FileService {
    private String serviceURL;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        String fileService = System.getenv("FILESERVICE");
        serviceURL = ("http://").concat(fileService).concat(":8080");
        
        webClient = WebClient.create();
    }
    
    public Map<String, String> uploadFile(
        UploadRequest request,
        String token
    ) 
        throws Exception
    {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", request.getFile().getResource());

        Map<String, String> response = webClient.post()
                            .uri(serviceURL.concat("/file"))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .body(BodyInserters.fromMultipartData(builder.build()))
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>(){})
                            .block();
        return response;
    }


    public List<?> listFiles(String token)
        throws Exception
    {
        List<?> response = webClient.get()
                            .uri(serviceURL.concat("/file"))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<List<Object>>(){})
                            .block();
        return response;
    }

    public Map<String, Long> processFile(
        String fileId,
        String token
    ) 
        throws Exception
    {
        System.out.println("fileId");
        System.out.println(fileId);
        System.out.println(token);

        Map<String, Long> response = webClient.post()
                            .uri(serviceURL.concat("/file/process"))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .bodyValue(Map.of("fileId", fileId))
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>(){})
                            .block();
                        
        return response;
    }

    public Map<String, String> getProcessStatus(
        Long id,
        String token
    )
        throws Exception
    {
        Map<String, String> response = webClient.get()
                            .uri(
                                serviceURL.concat("/file/process/{id}/status"), 
                                id
                            )
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>(){})
                            .block();
        return response;
    }

    public Map<String, String> restartProcess(
        Long id,
        String token
    )
        throws Exception
    {
        Map<String, String> response = webClient.post()
                            .uri(serviceURL.concat("/file/process/restart"))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .bodyValue(Map.of("id", id))
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>(){})
                            .block();
        return response;
    }
    
}
