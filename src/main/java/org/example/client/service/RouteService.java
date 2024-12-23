package org.example.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с маршрутами, включая получение, добавление и удаление маршрутов.
 */
@Service
public class RouteService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8081/api/routes";

    /**
     * Получает список всех маршрутов.
     *
     * @param token токен аутентификации для авторизации на сервере
     * @return список маршрутов в виде списка карт с данными маршрутов
     */
    public List<Map<String, Object>> getAllRoutes(String token) {
        String url = backendUrl + "/allRoutes";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return response.getBody();
    }

    /**
     * Добавляет новый маршрут.
     *
     * @param routeData данные маршрута (например, место отправления, место прибытия и длительность маршрута)
     * @param token токен аутентификации для авторизации на сервере
     * @param startLatitude широта начальной точки маршрута
     * @param startLongitude долгота начальной точки маршрута
     * @param endLatitude широта конечной точки маршрута
     * @param endLongitude долгота конечной точки маршрута
     */
    public void addRoute(Map<String, String> routeData, String token, String startLatitude, String startLongitude,
                         String endLatitude, String endLongitude) {

        String url = backendUrl + "/addRoute";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Собираем данные маршрута в объект
        Map<String, Object> route = Map.of(
                "placeOfDeparture", routeData.get("placeOfDeparture"),
                "placeOfArrival", routeData.get("placeOfArrival"),
                "routeDuration", routeData.get("routeDuration"),
                "startLatitude", startLatitude,
                "startLongitude", startLongitude,
                "endLatitude", endLatitude,
                "endLongitude", endLongitude
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(route, headers);

        restTemplate.postForEntity(url, entity, String.class);
    }

    /**
     * Удаляет маршрут по его идентификатору.
     *
     * @param id идентификатор маршрута
     * @param token токен аутентификации для авторизации на сервере
     */
    public void deleteRoute(Long id, String token) {
        String url = backendUrl + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
}
