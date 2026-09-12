package com.gateway.api.config;

import java.util.List;

import org.springframework.boot.context.config.ConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gateway.api.component.ApiControllerCsrfMatcher;
import com.gateway.api.config.JwtFilterChain;

@Configuration 
public class SecurityConfig{

    private final JwtFilterChain jwtFilterChain;

    public SecurityConfig(JwtFilterChain jwtFilterChain) {
        this.jwtFilterChain = jwtFilterChain;
    }
    
    @Bean
    public SecurityFilterChain ApiSecurityFilterChain(
        HttpSecurity http
    ) throws Exception {
        http
            .csrf( csrf -> csrf
                .requireCsrfProtectionMatcher(
                    new ApiControllerCsrfMatcher()
                )
                .csrfTokenRepository(
                    CookieCsrfTokenRepository.withHttpOnlyFalse()
                )
            )
            // .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                auth -> auth
                    .requestMatchers("/csrf").permitAll()
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                    ).permitAll()
                    .requestMatchers(
                        "/api/v1/register",
                        "/api/v1/login",
                        "/api/v1/logout"
                    ).permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtFilterChain,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // @Bean
    // public CorsConfigurationSource apiCorsConfig() {
    //     CorsConfiguration configuration = new CorsConfiguration();

    //     configuration.setAllowedOrigins(
    //         List.of(
    //             "http://localhost:3000"
    //         )
    //     );

    //     configuration.setAllowedMethods(
    //         List.of(
    //             "GET",
    //             "POST",
    //             "PUT",
    //             "DELETE"
    //         )
    //     );

    //     configuration.setAllowedHeaders(
    //         List.of(
    //             "Authorization",
    //             "Cache-Control",
    //             "Content-Type",
    //             "X-XSRF-TOKEN",
    //         )
    //     );

    //     configuration.setAllowCredentials(true);

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);

    //     return source;
    // }
}
