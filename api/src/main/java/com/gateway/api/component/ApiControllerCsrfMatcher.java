package com.gateway.api.component;

import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

public class ApiControllerCsrfMatcher
        implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {

        String method = request.getMethod();

        // Only protect state-changing requests
         if ("GET".equalsIgnoreCase(method)
            || "HEAD".equalsIgnoreCase(method)
            || "OPTIONS".equalsIgnoreCase(method)
            || "TRACE".equalsIgnoreCase(method)) {
            return false;
        }

        return true;
    }
}