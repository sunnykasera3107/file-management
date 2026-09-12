package com.manager.users.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.manager.users.model.UserJwt;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;


@Service
public class JwtService {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private long exp;
    
    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken(UserJwt user) {
        Instant time = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(issuer)
            .issuedAt(time)
            .expiresAt(time.plusSeconds(exp))
            .subject(user.getId().toString())
            .claim("username", user.getUsername())
            .claim("email", user.getEmail())
            .claim("roles", user.getAuthorities()
                                .stream()
                                .map(auth -> auth.getAuthority())
                                .toList()
            )
            .build();
        
        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

    }
    
}
