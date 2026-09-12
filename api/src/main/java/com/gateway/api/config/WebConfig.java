package com.gateway.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gateway.api.annotation.ApiController;

@Configuration 
public class WebConfig implements  WebMvcConfigurer {

    @Value("${spring.mcv.api.prefix}")
    private String apiPrefix;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(
            apiPrefix, 
            c -> c.isAnnotationPresent(ApiController.class)
        );
    }
    
}
