package org.example.client.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Сервис для аутентификации и регистрации пользователей.
 * Отправляет запросы на сервер аутентификации для выполнения операций регистрации и входа.
 */
@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8081/auth";

    /**
     * Регистрирует нового пользователя, отправляя данные на сервер аутентификации.
     *
     * @param user карта, содержащая данные пользователя для регистрации
     *             (например, имя пользователя, пароль и т. д.)
     * @return ответ от сервера в виде строки
     */
    public String registerUser(Map<String, Object> user) {
        String url = backendUrl + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    /**
     * Авторизует пользователя, отправляя данные для входа на сервер аутентификации.
     *
     * @param user карта, содержащая данные пользователя для входа
     *             (например, имя пользователя и пароль)
     * @return ответ от сервера в виде строки, содержащий, например, JWT токен
     */
    public String loginUser(Map<String, Object> user) {
        String url = backendUrl + "/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}
