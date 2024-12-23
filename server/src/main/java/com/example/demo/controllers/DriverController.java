package com.example.demo.controllers;

import com.example.demo.config.UserDetailsImpl;
import com.example.demo.models.Driver;
import com.example.demo.models.UserModel;
import com.example.demo.services.DriverService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления профилями водителей.
 * Обрабатывает запросы для получения, обновления, удаления и изменения данных водителей.
 */
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    /**
     * Получение списка всех водителей.
     * Доступно для пользователей с ролями ROLE_ADMIN и ROLE_MANAGER.
     *
     * @return {@link ResponseEntity} с списком водителей и HTTP статусом OK.
     */
    @GetMapping("/allDrivers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    /**
     * Получение профиля водителя для текущего авторизованного пользователя.
     * Доступно только для пользователей с ролью ROLE_DRIVER.
     *
     * @return {@link ResponseEntity} с профилем водителя и HTTP статусом OK, если профиль найден.
     *         Если профиль не найден, возвращается HTTP статус NOT_FOUND.
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<Driver> getDriverProfile() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userDetails.getUser();

        List<Driver> drivers = driverService.getDriversByUserId(user.getId());
        if (drivers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(drivers.get(0)); // Предполагается, что у одного пользователя может быть только один профиль
    }

    /**
     * Обновление профиля водителя для текущего авторизованного пользователя.
     * Если профиль уже существует, он будет обновлен, если нет — создан новый.
     * Доступно только для пользователей с ролью ROLE_DRIVER.
     *
     * @param driver объект {@link Driver}, содержащий обновленные данные профиля водителя.
     * @return {@link ResponseEntity} с сообщением об успешном обновлении или создании профиля.
     */
    @PostMapping("/profile")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<String> updateDriverProfile(@RequestBody Driver driver) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userDetails.getUser();

        // Проверка существующего профиля
        List<Driver> existingDrivers = driverService.getDriversByUserId(user.getId());
        if (!existingDrivers.isEmpty()) {
            // Обновление профиля водителя, если он уже существует
            Driver existingDriver = existingDrivers.get(0); // Предполагается, что один пользователь имеет один профиль
            existingDriver.setFullName(driver.getFullName());
            existingDriver.setDrivingLicense(driver.getDrivingLicense());
            existingDriver.setDrivingExperience(driver.getDrivingExperience());
            driverService.updateDriver(existingDriver.getId(), existingDriver);
            return ResponseEntity.ok("Профиль успешно обновлен.");
        }

        // Привязка нового профиля к текущему пользователю
        driver.setUserId(user.getId());
        driverService.createDriver(driver);

        return ResponseEntity.ok("Профиль успешно создан.");
    }

    /**
     * Обновление зарплаты водителя.
     * Доступно для пользователей с ролями ROLE_ADMIN и ROLE_MANAGER.
     *
     * @param id ID водителя, чья зарплата будет обновлена.
     * @param request объект {@link Map}, содержащий новую зарплату водителя.
     * @return {@link ResponseEntity} с сообщением о успешном обновлении зарплаты.
     */
    @PostMapping("/{id}/salary")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> updateDriverSalary(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        double salary = request.get("salary");
        driverService.updateSalary(id, salary);
        return ResponseEntity.ok("Зарплата обновлена");
    }

    /**
     * Извлечение роли пользователя из JWT токена.
     *
     * @param token строка, содержащая JWT токен с префиксом "Bearer ".
     * @return строка, представляющая роль пользователя.
     */
    @GetMapping("/getRoleFromToken")
    public String getRoleFromToken(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", ""); // Убираем "Bearer " из заголовка
        return extractUserRoleFromToken(jwt); // Используем метод для извлечения роли
    }

    /**
     * Извлекает роль пользователя из токена.
     *
     * @param token JWT токен.
     * @return строка с ролью пользователя.
     * @throws RuntimeException если не удалось извлечь роль из токена.
     */
    private String extractUserRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(parts[1]));
            Map<String, Object> claims = new ObjectMapper().readValue(payload, Map.class);
            return (String) claims.get("role");
        } catch (Exception e) {
            throw new RuntimeException("Не удалось извлечь роль из токена", e);
        }
    }

    /**
     * Удаление водителя по ID.
     * Доступно только для пользователей с ролью ROLE_ADMIN.
     *
     * @param id ID водителя, которого необходимо удалить.
     * @return {@link ResponseEntity} с HTTP статусом NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Получение информации о водителе по его ID.
     * Доступно для пользователей с ролями ROLE_ADMIN и ROLE_MANAGER.
     *
     * @param id ID водителя.
     * @return {@link ResponseEntity} с информацией о водителе и HTTP статусом OK.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id) {
        Driver driver = driverService.getDriverById(id);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }
}
