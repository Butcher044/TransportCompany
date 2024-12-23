package com.example.demo.controllers;

import com.example.demo.config.UserDetailsImpl;
import com.example.demo.models.Driver;
import com.example.demo.models.Manager;
import com.example.demo.models.UserModel;
import com.example.demo.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления профилями менеджеров.
 * Обрабатывает запросы для получения, обновления, удаления и изменения данных менеджеров.
 */
@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    /**
     * Получение списка всех менеджеров.
     * Доступно для пользователей с ролью ROLE_ADMIN.
     *
     * @return {@link ResponseEntity} с списком менеджеров и HTTP статусом OK.
     */
    @GetMapping("/allManagers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        return new ResponseEntity<>(managers, HttpStatus.OK);
    }

    /**
     * Обновление профиля менеджера для текущего авторизованного пользователя.
     * Если профиль уже существует, он будет обновлен, если нет — создан новый.
     * Доступно только для пользователей с ролью ROLE_MANAGER.
     *
     * @param manager объект {@link Manager}, содержащий обновленные данные профиля менеджера.
     * @return {@link ResponseEntity} с сообщением об успешном обновлении или создании профиля.
     */
    @PostMapping("/profile")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> updateManagerProfile(@RequestBody Manager manager) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userDetails.getUser();

        // Проверка существующего профиля
        List<Manager> existingManagers = managerService.getManagersByUserId(user.getId());
        if (!existingManagers.isEmpty()) {
            // Обновление профиля менеджера, если он уже существует
            Manager existingManager = existingManagers.get(0); // Предполагается, что один пользователь имеет один профиль
            existingManager.setFullName(manager.getFullName());
            existingManager.setDateOfBirth(manager.getDateOfBirth());
            existingManager.setPhoneNumber(manager.getPhoneNumber());
            existingManager.setResidenceAddress(manager.getResidenceAddress());
            managerService.updateManager(existingManager.getId(), existingManager);
            return ResponseEntity.ok("Профиль успешно обновлен.");
        }

        // Привязка нового профиля к текущему пользователю
        manager.setUserId(user.getId());
        managerService.createManager(manager);

        return ResponseEntity.ok("Профиль успешно создан.");
    }

    /**
     * Удаление менеджера по ID.
     * Доступно только для пользователей с ролью ROLE_ADMIN.
     *
     * @param id ID менеджера, которого необходимо удалить.
     * @return {@link ResponseEntity} с HTTP статусом NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Обновление зарплаты менеджера.
     * Доступно только для пользователей с ролью ROLE_ADMIN.
     *
     * @param id ID менеджера, чья зарплата будет обновлена.
     * @param request объект {@link Map}, содержащий новую зарплату менеджера.
     * @return {@link ResponseEntity} с сообщением о успешном обновлении зарплаты.
     */
    @PostMapping("/{id}/salary")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateManagerSalary(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        double salary = request.get("salary");
        managerService.updateSalary(id, salary);
        return ResponseEntity.ok("Зарплата обновлена");
    }

    /**
     * Получение информации о менеджере по его ID.
     * Доступно для пользователей с ролями ROLE_ADMIN.
     *
     * @param id ID менеджера.
     * @return {@link ResponseEntity} с информацией о менеджере и HTTP статусом OK.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Manager> getManagerById(@PathVariable Long id) {
        Manager manager = managerService.getManagerById(id);
        return new ResponseEntity<>(manager, HttpStatus.OK);
    }
}
