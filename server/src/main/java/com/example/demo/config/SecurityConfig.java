package com.example.demo.config;

import com.example.demo.JWT.JwtFilter;
import com.example.demo.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности для приложения, включая настройку аутентификации и авторизации.
 * Этот класс конфигурирует фильтры безопасности, аутентификацию пользователей,
 * а также алгоритмы шифрования паролей.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Конструктор для инициализации фильтра JWT и пользовательского сервиса деталей.
     *
     * @param jwtFilter фильтр для обработки JWT токенов.
     * @param userDetailsService сервис для загрузки данных пользователя.
     */
    public SecurityConfig(JwtFilter jwtFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Настройка цепочки фильтров безопасности.
     * Конфигурирует доступ к URL и применяет фильтр JWT перед фильтром аутентификации.
     *
     * @param http объект для настройки HTTP безопасности.
     * @return настроенная цепочка фильтров безопасности.
     * @throws Exception если возникает ошибка при настройке безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  // Отключаем защиту от CSRF для упрощения взаимодействия с API.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register").permitAll()  // Разрешаем доступ без аутентификации.
                        .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации.
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Добавляем фильтр JWT до фильтра аутентификации.

        return http.build();
    }

    /**
     * Создаёт и возвращает экземпляр менеджера аутентификации с использованием предоставленного
     * сервиса деталей пользователя и энкодера паролей.
     *
     * @param userDetailsService сервис для загрузки данных пользователя.
     * @param passwordEncoder энкодер паролей для безопасного хеширования.
     * @return менеджер аутентификации для проверки данных пользователя.
     * @throws Exception если возникает ошибка при создании менеджера аутентификации.
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    /**
     * Создаёт и возвращает энкодер паролей с использованием алгоритма BCrypt.
     *
     * @return энкодер паролей для безопасного хеширования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Используется алгоритм BCrypt для шифрования паролей.
    }

    /**
     * Создаёт и возвращает поставщика аутентификации с использованием сервиса данных пользователей и энкодера паролей.
     *
     * @param userDetailsService сервис для загрузки данных пользователя.
     * @return поставщик аутентификации, который использует сервис пользователя и энкодер паролей.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());  // Устанавливаем энкодер для безопасного хеширования паролей.
        return authProvider;
    }
}
