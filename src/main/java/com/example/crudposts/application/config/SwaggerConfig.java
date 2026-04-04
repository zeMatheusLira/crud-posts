package com.example.crudposts.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Starti 2026 - API de Publicações")
                        .version("1.0")
                        .description("API REST para o sistema simplificado de publicações e comentários."));
    }
}