package org.example.client.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с грузовиками, включая получение всех грузовиков, добавление нового и удаление существующего.
 */
@Service
public class TruckService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8081/api/trucks";

    /**
     * Получает список всех грузовиков.
     *
     * @param token токен аутентификации для авторизации на сервере
     * @return список грузовиков в виде списка карт с данными о грузовиках
     */
    public List<Map<String, Object>> getAllTrucks(String token) {
        String url = backendUrl + "/allTrucks";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return response.getBody();
    }

    /**
     * Добавляет новый грузовик.
     *
     * @param truckData данные грузовика (например, марка, модель, год выпуска и другие параметры)
     * @param token токен аутентификации для авторизации на сервере
     */
    public void addTruck(Map<String, String> truckData, String token) {
        String url = backendUrl + "/addTruck";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(truckData, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    /**
     * Удаляет грузовик по его идентификатору.
     *
     * @param id идентификатор грузовика
     * @param token токен аутентификации для авторизации на сервере
     */
    public void deleteTruck(Long id, String token) {
        String url = backendUrl + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
}
