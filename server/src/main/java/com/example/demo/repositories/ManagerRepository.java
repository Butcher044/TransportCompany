package com.example.demo.repositories;

import com.example.demo.models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Manager}.
 * Содержит методы для взаимодействия с таблицей менеджеров в базе данных.
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    /**
     * Находит всех менеджеров по идентификатору пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список менеджеров, связанных с указанным пользователем.
     */
    List<Manager> findAllByUserId(Long userId);
}
