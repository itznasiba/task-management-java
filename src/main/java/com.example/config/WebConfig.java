package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com")
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    // Əgər gələcəkdə html/jsp səhifələri əlavə etmək istəsən, ViewResolver-i bura yazacaqsan.
}