package com.sprache.juandiegodeutsch.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig {


    // Configuration for the Cross-Origin Resource Sharing (CORS) settings for the endpoints localhost and the deployed link
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3001","https://frontend-notes-production.up.railway.app","http://192.168.213.114:3001"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Habilitar CORS para todas las rutas

        return source;
    }


    // Alternative configuration for handling CORS mappings at the WebMvc level
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permitir todos los endpoints
                        .allowedOrigins("*") // Permitir todos los orígenes
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH") // Métodos permitidos
                        .allowedHeaders("*") // Permitir todos los encabezados
                        .allowCredentials(false); // No se necesitan credenciales si permites todo
            }
        };
    }

}


