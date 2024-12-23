package org.example.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления данными водителей, включая создание, обновление профилей, получение информации и удаление.
 * Также включает методы для работы с зарплатой водителей и извлечения роли пользователя из JWT токена.
 */
@Service
public class DriverService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8081/api/drivers";

    /**
     * Создает новый профиль водителя.
     *
     * @param profileData данные профиля водителя (например, имя, стаж, данные о водительских правах и т. д.)
     * @param token токен аутентификации для авторизации на сервере
     */
    public void createDriverProfile(Map<String, String> profileData, String token) {
        String url = backendUrl + "/profile";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(profileData, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    /**
     * Получает список всех водителей.
     *
     * @param token токен аутентификации для авторизации на сервере
     * @return список водителей в виде списка карт с данными
     */
    public List<Map<String, Object>> getAllDrivers(String token) {
        String url = backendUrl + "/allDrivers";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return response.getBody();
    }

    /**
     * Получает информацию о водителе по его идентификатору.
     *
     * @param id идентификатор водителя
     * @param token токен аутентификации для авторизации на сервере
     * @return данные водителя в виде карты
     */
    public Map<String, Object> getDriverById(Long id, String token) {
        String url = backendUrl + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    /**
     * Создает или обновляет профиль водителя.
     *
     * @param profileData данные профиля водителя
     * @param token токен аутентификации для авторизации на сервере
     */
    public void createOrUpdateDriverProfile(Map<String, String> profileData, String token) {
        String url = backendUrl + "/profile";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(profileData, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    /**
     * Устанавливает зарплату для водителя.
     *
     * @param id идентификатор водителя
     * @param salary новая зарплата водителя
     * @param token токен аутентификации для авторизации на сервере
     */
    public void setDriverSalary(Long id, double salary, String token) {
        String url = backendUrl + "/" + id + "/salary";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        Map<String, Object> body = Map.of("salary", salary);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    /**
     * Удаляет информацию о водителе.
     *
     * @param id идентификатор водителя
     * @param token токен аутентификации для авторизации на сервере
     */
    public void deleteDriver(Long id, String token) {
        String url = backendUrl + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    /**
     * Извлекает роль пользователя из JWT токена.
     *
     * @param token JWT токен
     * @return роль пользователя, извлеченная из токена
     * @throws RuntimeException если не удалось извлечь роль из токена
     */
    private String extractUserRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\."); // JWT состоит из 3 частей: заголовок, полезная нагрузка, подпись
            String payload = new String(Base64.getDecoder().decode(parts[1])); // Декодируем payload
            Map<String, Object> claims = new ObjectMapper().readValue(payload, Map.class); // Парсим JSON
            return (String) claims.get("role"); // Извлекаем роль из claim "role"
        } catch (Exception e) {
            throw new RuntimeException("Не удалось извлечь роль из токена: " + e.getMessage());
        }
    }

}
