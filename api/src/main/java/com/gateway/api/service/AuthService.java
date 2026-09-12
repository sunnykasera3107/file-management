package com.gateway.api.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gateway.api.dto.login.LoginRequest;
import com.gateway.api.dto.register.RegisterRequest;

import jakarta.annotation.PostConstruct;

@Service 
public class AuthService {

    private String userServiceURL;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        String userService = System.getenv("USERSERVICE");
        userServiceURL = ("http://").concat(userService).concat(":8080");
        
        webClient = WebClient.create();
    }
    
    public Map<String, String> registerUser(RegisterRequest request) 
        throws Exception
    {
        Map<String, String> response = webClient.post()
                            .uri(userServiceURL.concat("/user/register"))
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>(){})
                            .block();
        return response;
    }

    public Map<String, String> loginUser(LoginRequest request) 
        throws UsernameNotFoundException, Exception
    {
        Map<String, String> response = webClient.post()
                            .uri(userServiceURL.concat("/login"))
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>(){})
                            .block();
        return response;
    }
}
