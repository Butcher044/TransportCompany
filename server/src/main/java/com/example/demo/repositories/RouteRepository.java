package com.example.demo.repositories;

import com.example.demo.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Route}.
 * Содержит методы для взаимодействия с таблицей маршрутов в базе данных.
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    /**
     * Находит все маршруты, созданные пользователем с указанным идентификатором.
     *
     * @param userId Идентификатор пользователя, который создал маршруты.
     * @return Список маршрутов, созданных указанным пользователем.
     */
    List<Route> findAllByCreatedBy(Long userId);
}
