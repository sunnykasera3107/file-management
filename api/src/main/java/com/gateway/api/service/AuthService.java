package com.gateway.api.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gateway.api.dto.GeneralResponse;
import com.gateway.api.dto.user.LoginRequest;
import com.gateway.api.dto.user.RegisterRequest;
import com.gateway.api.dto.user.UserResponse;

import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;

@Service 
public class AuthService {

    private String userServiceURL;

    private WebClient webClient;

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostConstruct
    public void init() {
        String userService = System.getenv("USERSERVICE");
        userServiceURL = ("http://").concat(userService).concat(":8080");
        
        webClient = WebClient.create();
    }
    
    public GeneralResponse registerUser(
        RegisterRequest request
    ) {
        return webClient.post()
            .uri(userServiceURL.concat("/user/register"))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<GeneralResponse>(){})
            .block();
    }

    public GeneralResponse loginUser(
        LoginRequest request
    ) {
        return webClient.post()
            .uri(userServiceURL.concat("/login"))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<GeneralResponse>(){})
            .block();
    }

    public UserResponse getUser(
        String token
    ) {
        Claims claims = jwtService.extractToken(token);
        return webClient.get()
            .uri(
                userServiceURL.concat("/user/{id}"), 
                claims.getSubject().toString()
            )
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<UserResponse>(){})
            .block();
    }
}
