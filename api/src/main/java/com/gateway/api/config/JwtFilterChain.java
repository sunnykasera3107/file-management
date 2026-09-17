package com.gateway.api.config;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gateway.api.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilterChain extends OncePerRequestFilter{

    private final JwtService jwtService;

    public JwtFilterChain(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String uri = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        return uri.startsWith("/swagger-ui/")
                || uri.startsWith("/v3/api-docs/")
                || uri.equals("/swagger-ui.html")
                || uri.equals("/api/v1/csrf")
                || uri.equals("/api/v1/login")
                || uri.equals("/api/v1/register")
                || uri.equals("/api/v1/logout");
    }

    @Override
    public void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws IOException, ServletException{

        String token = getJwtFromCookie(request);
        
        try{
            if (token != null) {
                Claims claims = jwtService.extractToken(token);
                String userId = claims.getSubject();

                request.setAttribute("access_token", token);
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.emptyList()
                    );

                SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {
            clearAccessTokenCookie(response);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                {"error":"TOKEN_EXPIRED"}
            """);
        }

        filterChain.doFilter(request, response);

    }

    private String getJwtFromCookie(
        HttpServletRequest request
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (("access_token").equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private void clearAccessTokenCookie(
            HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie
                .from("access_token", "")
                .httpOnly(true)
                .secure(false) // true in production HTTPS
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite("Lax")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }
        
}
