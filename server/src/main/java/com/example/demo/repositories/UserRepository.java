package com.example.demo.repositories;

import com.example.demo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link UserModel}.
 * Содержит методы для взаимодействия с таблицей пользователей в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    /**
     * Находит пользователя по имени пользователя (username).
     *
     * @param username Имя пользователя для поиска.
     * @return Опционально возвращает пользователя, если он существует.
     */
    Optional<UserModel> findByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя (username).
     *
     * @param username Имя пользователя для проверки.
     * @return {@code true}, если пользователь с таким именем существует, иначе {@code false}.
     */
    boolean existsByUsername(String username);
}
