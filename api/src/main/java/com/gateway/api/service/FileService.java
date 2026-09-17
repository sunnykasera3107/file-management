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

import com.gateway.api.dto.GeneralResponse;
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
    
    public GeneralResponse uploadFile(
        UploadRequest request,
        String token
    ) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", request.getFile().getResource());

        return webClient.post()
            .uri(serviceURL.concat("/file"))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(builder.build()))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<GeneralResponse>(){})
            .block();
    }


    public List<?> listFiles(
        String token
    ) {
        return webClient.get()
            .uri(serviceURL.concat("/file"))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Object>>(){})
            .block();
    }

    public Map<String, Object> processFile(
        String fileId,
        String token
    ) {
        return webClient.post()
            .uri(serviceURL.concat("/file/process"))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .bodyValue(Map.of("fileId", fileId))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>(){})
            .block();
    }

    public GeneralResponse getProcessStatus(
        Long id,
        String token
    ) {
        return webClient.get()
            .uri(
                serviceURL.concat("/file/process/{id}/status"), 
                id
            )
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<GeneralResponse>(){})
            .block();
    }

    public GeneralResponse restartProcess(
        Long id,
        String token
    ) {
        return webClient.post()
            .uri(serviceURL.concat("/file/process/restart"))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .bodyValue(Map.of("id", id))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<GeneralResponse>(){})
            .block();
    }

    public Map<String, Object> getFile(
        String id,
        String token
    ) {
        return webClient.get()
            .uri(
                serviceURL.concat("/file/{id}"), 
                id
            )
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>(){})
            .block();
    }    
}
