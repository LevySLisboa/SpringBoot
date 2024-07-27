package com.example.api_gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(new Info().title("RESTful API with Java 21 and Spring Boot 3.3.2").version("v1").description("Spring Boot Learning Project")
                .termsOfService("").license(new License().name("").url("")));

    }
}
