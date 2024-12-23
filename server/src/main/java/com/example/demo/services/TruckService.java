package com.example.demo.services;

import com.example.demo.models.Truck;
import com.example.demo.repositories.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с сущностью {@link Truck}.
 * Обрабатывает логику получения, создания, обновления и удаления грузовиков.
 */
@Service
public class TruckService {

    @Autowired
    private TruckRepository truckRepository;

    /**
     * Получает список всех грузовиков.
     *
     * @return Список всех грузовиков.
     */
    public List<Truck> getAllTrucks() {
        return truckRepository.findAll();
    }


    /**
     * Создает новый грузовик.
     *
     * @param truck Объект {@link Truck}, который нужно создать.
     * @return Созданный объект {@link Truck}.
     */
    public Truck createTruck(Truck truck) {
        return truckRepository.save(truck);
    }

    /**
     * Получает грузовик по его ID.
     *
     * @param id ID грузовика.
     * @return Объект {@link Truck} с указанным ID.
     * @throws RuntimeException если грузовик с таким ID не найден.
     */
    public Truck getTruckById(Long id) {
        return truckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Грузовик с id не найден: " + id));
    }

    /**
     * Удаляет грузовик по его ID.
     *
     * @param id ID грузовика, который нужно удалить.
     */
    public void deleteTruck(Long id) {
        truckRepository.deleteById(id);
    }
}
