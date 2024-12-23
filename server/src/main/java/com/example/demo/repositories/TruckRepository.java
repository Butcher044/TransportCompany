package com.example.demo.repositories;

import com.example.demo.models.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Truck}.
 * Содержит методы для взаимодействия с таблицей грузовиков в базе данных.
 */
@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {

    /**
     * Находит все грузовики, созданные пользователем с указанным идентификатором.
     *
     * @param userId Идентификатор пользователя, который создал грузовики.
     * @return Список грузовиков, созданных указанным пользователем.
     */
    List<Truck> findAllByCreatedBy(Long userId);
}
