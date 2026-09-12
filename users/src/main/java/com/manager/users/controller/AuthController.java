package com.manager.users.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.manager.users.dto.LoginRequest;
import com.manager.users.dto.LoginResponse;
import com.manager.users.model.UserJwt;
import com.manager.users.service.JwtService;

@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired(required=true)
    private AuthenticationManager authenticationManager;
    
    @PostMapping("/login")
    public LoginResponse login(
        @RequestBody LoginRequest request
    ) {
        Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );

        UserJwt userJwt = (UserJwt) authentication.getPrincipal();

        String token = jwtService.generateToken(userJwt);
        
        return new LoginResponse(
            token
        );
    }
}
