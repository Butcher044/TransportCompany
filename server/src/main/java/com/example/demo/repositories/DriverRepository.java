package com.example.demo.repositories;

import com.example.demo.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Driver}.
 * Содержит методы для взаимодействия с таблицей водителей в базе данных.
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    /**
     * Находит всех водителей по идентификатору пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список водителей, связанных с указанным пользователем.
     */
    List<Driver> findAllByUserId(Long userId);
}
