package com.example.demo.services;

import com.example.demo.models.Driver;
import com.example.demo.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с сущностью {@link Driver}.
 * Обрабатывает логику получения, создания, обновления и удаления водителей.
 */
@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    /**
     * Получает список всех водителей.
     *
     * @return Список всех водителей.
     */
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    /**
     * Получает список водителей, принадлежащих конкретному пользователю.
     *
     * @param userId ID пользователя.
     * @return Список водителей, принадлежащих пользователю.
     */
    public List<Driver> getDriversByUserId(Long userId) {
        return driverRepository.findAllByUserId(userId);
    }

    /**
     * Создает нового водителя.
     *
     * @param driver Объект {@link Driver}, который нужно создать.
     * @return Созданный объект {@link Driver}.
     */
    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    /**
     * Получает водителя по его ID.
     *
     * @param id ID водителя.
     * @return Объект {@link Driver} с указанным ID.
     * @throws RuntimeException если водитель с таким ID не найден.
     */
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Водитель с id не найден: " + id));
    }

    /**
     * Обновляет зарплату водителя.
     *
     * @param id ID водителя.
     * @param salary Новая зарплата водителя.
     * @throws RuntimeException если водитель с таким ID не найден.
     */
    public void updateSalary(Long id, double salary) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Водитель не найден"));
        driver.setSalary(salary);
        driverRepository.save(driver);
    }

    /**
     * Обновляет данные водителя.
     *
     * @param id ID водителя, данные которого нужно обновить.
     * @param updatedDriver Объект {@link Driver} с новыми данными.
     * @return Обновленный объект {@link Driver}.
     * @throws RuntimeException если водитель с таким ID не найден.
     */
    public Driver updateDriver(Long id, Driver updatedDriver) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Водитель с id не найден: " + id));
        driver.setFullName(updatedDriver.getFullName());
        driver.setDrivingLicense(updatedDriver.getDrivingLicense());
        driver.setDrivingExperience(updatedDriver.getDrivingExperience());
        return driverRepository.save(driver);
    }

    /**
     * Удаляет водителя по его ID.
     *
     * @param id ID водителя, которого нужно удалить.
     */
    public void deleteDriver(Long id) {
        driverRepository.deleteById(id);
    }
}
