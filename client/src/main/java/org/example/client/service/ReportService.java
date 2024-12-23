package org.example.client.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Сервис для управления отчетами, включая получение, добавление и удаление отчетов.
 */
@Service
public class ReportService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8081/api/reports";

    /**
     * Получает список всех отчетов.
     *
     * @param token токен аутентификации для авторизации на сервере
     * @return список отчетов в виде списка карт с данными
     */
    public List<Map<String, Object>> getAllReports(String token) {
        String url = backendUrl + "/allReports";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return response.getBody();
    }

    /**
     * Добавляет новый отчет.
     *
     * @param truckData данные отчета (например, информация о транспортном средстве)
     * @param token токен аутентификации для авторизации на сервере
     */
    public void addReport(Map<String, String> truckData, String token) {
        String url = backendUrl + "/addReport";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(truckData, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    /**
     * Удаляет отчет по его идентификатору.
     *
     * @param id идентификатор отчета
     * @param token токен аутентификации для авторизации на сервере
     */
    public void deleteReport(Long id, String token) {
        String url = backendUrl + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
}
