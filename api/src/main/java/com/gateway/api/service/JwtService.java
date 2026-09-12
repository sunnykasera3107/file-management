package com.gateway.api.service;

import java.security.PublicKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service 
public class JwtService {

    private final PublicKey publicKey;

    public JwtService(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    
    public Claims extractToken(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
