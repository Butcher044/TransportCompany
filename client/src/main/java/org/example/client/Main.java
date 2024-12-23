package org.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения, который запускает Spring Boot приложение.
 * Этот класс содержит точку входа для приложения.
 */
@SpringBootApplication
public class Main {

    /**
     * Точка входа в приложение.
     * Запускает Spring Boot приложение.
     *
     * @param args аргументы командной строки, передаваемые приложению при запуске
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
