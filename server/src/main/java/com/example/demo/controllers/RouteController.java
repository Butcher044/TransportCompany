package com.example.demo.controllers;

import com.example.demo.config.UserDetailsImpl;
import com.example.demo.models.Route;
import com.example.demo.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Контроллер для управления маршрутами.
 * Обрабатывает запросы для получения списка маршрутов, добавления, удаления и получения маршрута по ID.
 */
@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * Получение списка всех маршрутов.
     * Доступно для пользователей с ролями ROLE_ADMIN, ROLE_MANAGER и ROLE_DRIVER.
     *
     * @return {@link ResponseEntity} с списком маршрутов и HTTP статусом OK.
     */
    @GetMapping("/allRoutes")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DRIVER')")
    public ResponseEntity<List<Route>> getAllRoutes() {
        // Получение всех маршрутов без фильтрации по пользователю
        List<Route> routes = routeService.getAllRoutes();
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    /**
     * Добавление нового маршрута.
     * Доступно для пользователей с ролями ROLE_ADMIN и ROLE_MANAGER.
     * Устанавливает ID текущего пользователя как владельца маршрута.
     *
     * @param route объект {@link Route}, который содержит данные для добавления нового маршрута.
     * @return {@link ResponseEntity} с сохраненным маршрутом и HTTP статусом CREATED.
     */
    @PostMapping("/addRoute")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Route> addRoute(@RequestBody Route route) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        route.setCreatedBy(userDetails.getUser().getId()); // Устанавливаем владельца записи

        // Вычисление времени маршрута в минутах
        long routeDurationInMinutes = route.getRouteDuration();

        // Устанавливаем продолжительность маршрута
        route.setRouteDuration(routeDurationInMinutes);

        // Сохраняем маршрут
        Route savedRoute = routeService.createRoute(route);
        return new ResponseEntity<>(savedRoute, HttpStatus.CREATED);
    }

    /**
     * Удаление маршрута по ID.
     * Доступно для пользователей с ролями ROLE_ADMIN и ROLE_MANAGER.
     *
     * @param id ID маршрута, который необходимо удалить.
     * @return {@link ResponseEntity} с HTTP статусом NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Получение маршрута по ID.
     * Доступно для пользователей с ролями ROLE_ADMIN, ROLE_MANAGER и ROLE_DRIVER.
     *
     * @param id ID маршрута, который необходимо получить.
     * @return {@link ResponseEntity} с маршрутом и HTTP статусом OK.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DRIVER')")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        Route route = routeService.getRouteById(id);
        return new ResponseEntity<>(route, HttpStatus.OK);
    }
}
