package com.manager.files.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain customFilterChain(
        HttpSecurity http
    ) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource apiCorsConfig() {
        CorsConfiguration configuration = new CorsConfiguration();

        String serviceURL = System.getenv("APIGATEWAY");
        serviceURL = ("http://").concat(serviceURL).concat(":8080");

        configuration.setAllowedOrigins(
            List.of(
                serviceURL
            )
        );

        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE"
            )
        );

        configuration.setAllowedHeaders(
            List.of(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "X-XSRF-TOKEN"
            )
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
