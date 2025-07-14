

package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${ALLOWED_ORIGINS:*}")  // fallback to "*" if not set
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Split by comma in case you want multiple origins
                String[] origins = allowedOrigins.split(",");

                registry.addMapping("/**")              // match all endpoints
                        .allowedOrigins(origins)        // use env variable(s)
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
