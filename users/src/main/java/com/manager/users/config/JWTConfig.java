package com.manager.users.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;


@Configuration
public class JWTConfig {

    @Bean
    public JwtEncoder jwtEncoder(
        RSAPublicKey publicKey,
        RSAPrivateKey privateKey
    ) {
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .build();
        
        JWKSource<SecurityContext> jwkSource = 
            new ImmutableJWKSet<>( new JWKSet(rsaKey) ); 
            
        return new NimbusJwtEncoder(jwkSource);
    
    }

    @Bean
    public RSAPublicKey publicKey() throws Exception {
        String key = new String(
            new ClassPathResource("keys/public.pem")
                .getInputStream()
                .readAllBytes()
        );

        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec =
                new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory =
                KeyFactory.getInstance("RSA");

        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    @Bean
    public RSAPrivateKey privateKey() throws Exception {

        String key = new String(
                new ClassPathResource("keys/private.pem")
                        .getInputStream()
                        .readAllBytes()
        );

        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec =
                new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory =
                KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
