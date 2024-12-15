package com.sprache.juandiegodeutsch.config;


import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;

@Configuration
public class SwaggerConfig {



    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Token";

        return new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0.0")
                        .description("Documentación de la API con autenticación por Bearer Token"))

                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));
    }

}
