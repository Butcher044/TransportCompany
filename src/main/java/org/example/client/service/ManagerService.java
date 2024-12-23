package org.example.client.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Сервис для управления данными менеджеров, включая создание, обновление профилей, получение информации и удаление.
 * Также включает методы для работы с зарплатой менеджеров.
 */
@Service
public class ManagerService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8081/api/managers";

    /**
     * Создает новый профиль менеджера.
     *
     * @param profileData данные профиля менеджера (например, имя, контактная информация и т. д.)
     * @param token токен аутентификации для авторизации на сервере
     */
    public void createManagerProfile(Map<String, String> profileData, String token) {
        String url = backendUrl + "/profile";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(profileData, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    /**
     * Создает или обновляет профиль менеджера.
     *
     * @param profileData данные профиля менеджера
     * @param token токен аутентификации для авторизации на сервере
     */
    public void createOrUpdateManagerProfile(Map<String, String> profileData, String token) {
        String url = backendUrl + "/profile";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(profileData, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    /**
     * Получает список всех менеджеров.
     *
     * @param token токен аутентификации для авторизации на сервере
     * @return список менеджеров в виде списка карт с данными
     */
    public List<Map<String, Object>> getAllManagers(String token) {
        String url = backendUrl + "/allManagers";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return response.getBody();
    }

    /**
     * Получает информацию о менеджере по его идентификатору.
     *
     * @param id идентификатор менеджера
     * @param token токен аутентификации для авторизации на сервере
     * @return данные менеджера в виде карты
     */
    public Map<String, Object> getManagerById(Long id, String token) {
        String url = backendUrl + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    /**
     * Устанавливает зарплату для менеджера.
     *
     * @param id идентификатор менеджера
     * @param salary новая зарплата менеджера
     * @param token токен аутентификации для авторизации на сервере
     */
    public void setManagerSalary(Long id, double salary, String token) {
        String url = backendUrl + "/" + id + "/salary";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        Map<String, Object> body = Map.of("salary", salary);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    /**
     * Удаляет информацию о менеджере.
     *
     * @param id идентификатор менеджера
     * @param token токен аутентификации для авторизации на сервере
     */
    public void deleteManager(Long id, String token) {
        String url = backendUrl + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

}
