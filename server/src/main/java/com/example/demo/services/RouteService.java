package com.example.demo.services;

import com.example.demo.models.Route;
import com.example.demo.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с сущностью {@link Route}.
 * Обрабатывает логику получения, создания, удаления и получения маршрутов по пользователю.
 */
@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    /**
     * Создает новый маршрут.
     *
     * @param route Объект {@link Route}, который нужно создать.
     * @return Созданный объект {@link Route}.
     */
    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    /**
     * Получает маршрут по его ID.
     *
     * @param id ID маршрута.
     * @return Объект {@link Route} с указанным ID.
     * @throws RuntimeException если маршрут с таким ID не найден.
     */
    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Маршрут с id не найден: " + id));
    }

    /**
     * Удаляет маршрут по его ID.
     *
     * @param id ID маршрута, который нужно удалить.
     */
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    /**
     * Получает список всех маршрутов.
     *
     * @return Список всех маршрутов.
     */
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
}
