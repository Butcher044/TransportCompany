package com.example.demo.services;

import com.example.demo.models.Driver;
import com.example.demo.models.Manager;
import com.example.demo.repositories.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с сущностью {@link Manager}.
 * Обрабатывает логику получения, создания, обновления и удаления менеджеров.
 */
@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    /**
     * Получает список всех менеджеров.
     *
     * @return Список всех менеджеров.
     */
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    /**
     * Получает список менеджеров, принадлежащих конкретному пользователю.
     *
     * @param userId ID пользователя.
     * @return Список менеджеров, принадлежащих пользователю.
     */
    public List<Manager> getManagersByUserId(Long userId) {
        return managerRepository.findAllByUserId(userId);
    }

    /**
     * Создает нового менеджера.
     *
     * @param manager Объект {@link Manager}, который нужно создать.
     * @return Созданный объект {@link Manager}.
     */
    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    /**
     * Получает менеджера по его ID.
     *
     * @param id ID менеджера.
     * @return Объект {@link Manager} с указанным ID.
     * @throws RuntimeException если менеджер с таким ID не найден.
     */
    public Manager getManagerById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Менеджер с id не найден: " + id));
    }

    /**
     * Обновляет зарплату менеджера.
     *
     * @param id ID менеджера.
     * @param salary Новая зарплата менеджера.
     * @throws RuntimeException если менеджер с таким ID не найден.
     */
    public void updateSalary(Long id, double salary) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Менеджер не найден"));
        manager.setSalary(salary);
        managerRepository.save(manager);
    }

    /**
     * Обновляет данные менеджера.
     *
     * @param id ID менеджера, данные которого нужно обновить.
     * @param updatedManager Объект {@link Manager} с новыми данными.
     * @return Обновленный объект {@link Manager}.
     * @throws RuntimeException если менеджер с таким ID не найден.
     */
    public Manager updateManager(Long id, Manager updatedManager) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Менеджер с id не найден: " + id));
        manager.setFullName(updatedManager.getFullName());
        manager.setDateOfBirth(updatedManager.getDateOfBirth());
        manager.setPhoneNumber(updatedManager.getPhoneNumber());
        manager.setResidenceAddress(updatedManager.getResidenceAddress());
        return managerRepository.save(manager);
    }

    /**
     * Удаляет менеджера по его ID.
     *
     * @param id ID менеджера, которого нужно удалить.
     */
    public void deleteManager(Long id) {
        managerRepository.deleteById(id);
    }
}
