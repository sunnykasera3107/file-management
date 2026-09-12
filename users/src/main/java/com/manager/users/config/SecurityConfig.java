package com.manager.users.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final LogChainLink logChainLink;

    public SecurityConfig(LogChainLink logChainLink) {
        this.logChainLink = logChainLink;
    }
    
    @Bean
    public SecurityFilterChain customFilterChain(
        HttpSecurity http
    ) throws Exception {
        http
            .csrf(
                (csrf) -> csrf.disable()
            )
            .addFilterBefore(
                logChainLink,
                DisableEncodeUrlFilter.class
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .requestMatchers("/user/register").permitAll()
                .anyRequest().authenticated()
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
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


