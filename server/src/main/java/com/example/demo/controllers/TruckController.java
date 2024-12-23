package com.example.demo.controllers;

import com.example.demo.config.UserDetailsImpl;
import com.example.demo.models.Truck;
import com.example.demo.services.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления грузовиками.
 * Обрабатывает запросы для получения списка грузовиков, добавления, удаления и получения грузовика по ID.
 */
@RestController
@RequestMapping("/api/trucks")
public class TruckController {

    @Autowired
    private TruckService truckService;

    /**
     * Получение списка всех грузовиков.
     * Доступно для пользователей с ролями ROLE_ADMIN, ROLE_MANAGER и ROLE_DRIVER.
     *
     * @return {@link ResponseEntity} с списком грузовиков и HTTP статусом OK.
     */
    @GetMapping("/allTrucks")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DRIVER')")
    public ResponseEntity<List<Truck>> getAllTrucks() {
        List<Truck> trucks = truckService.getAllTrucks();
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    /**
     * Добавление нового грузовика.
     * Доступно для пользователей с ролями ROLE_ADMIN и ROLE_MANAGER.
     * Устанавливает ID текущего пользователя как владельца грузовика.
     *
     * @param truck объект {@link Truck}, который содержит данные для добавления нового грузовика.
     * @return {@link ResponseEntity} с сохраненным грузовиком и HTTP статусом CREATED.
     */
    @PostMapping("/addTruck")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Truck> addTruck(@RequestBody Truck truck) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        truck.setCreatedBy(userDetails.getUser().getId()); // Устанавливаем владельца записи

        Truck savedTruck = truckService.createTruck(truck);
        return new ResponseEntity<>(savedTruck, HttpStatus.CREATED);
    }

    /**
     * Удаление грузовика по ID.
     * Доступно для пользователей с ролями ROLE_ADMIN и ROLE_MANAGER.
     *
     * @param id ID грузовика, который необходимо удалить.
     * @return {@link ResponseEntity} с HTTP статусом NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteTruck(@PathVariable Long id) {
        truckService.deleteTruck(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Получение грузовика по ID.
     * Доступно для пользователей с ролями ROLE_ADMIN, ROLE_MANAGER и ROLE_DRIVER.
     *
     * @param id ID грузовика, который необходимо получить.
     * @return {@link ResponseEntity} с грузовиком и HTTP статусом OK.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DRIVER')")
    public ResponseEntity<Truck> getTruckById(@PathVariable Long id) {
        Truck truck = truckService.getTruckById(id);
        return new ResponseEntity<>(truck, HttpStatus.OK);
    }
}
