package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс для запуска приложения транспортной компании.
 * Это точка входа в приложение Spring Boot.
 */
@SpringBootApplication
public class TransportCompanyApplication {

	/**
	 * Главный метод для запуска приложения.
	 * Запускает приложение Spring Boot с помощью {@link SpringApplication}.
	 *
	 * @param args Аргументы командной строки, передаваемые в приложение.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TransportCompanyApplication.class, args);
	}

}
