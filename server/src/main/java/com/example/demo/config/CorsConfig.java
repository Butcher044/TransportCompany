package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация CORS (Cross-Origin Resource Sharing) для разрешения доступа
 * к ресурсам из разных источников.
 *
 * Этот класс настраивает разрешения для выполнения запросов между
 * клиентом и сервером с разных доменов.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Метод для настройки CORS.
     * Указывает разрешённые источники, методы и заголовки для запросов.
     *
     * @param registry реестр CORS, через который настраиваются правила
     *                для разных путей и источников.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Разрешаем доступ ко всем путям
                .allowedOrigins("http://localhost:8080")  // Укажите адрес клиента
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Разрешаем определённые методы
                .allowedHeaders("*")  // Разрешаем все заголовки
                .allowCredentials(true);  // Разрешаем передачу cookie и заголовков
    }
}
