package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

/**
 * Класс, представляющий пользователя в системе.
 * Содержит информацию о пользователе, такую как имя, пароль и роли.
 */
@Data
@Entity
@Table(name = "users")
public class UserModel {

    /**
     * Уникальный идентификатор пользователя.
     * Это значение автоматически генерируется в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя.
     * Это поле должно быть уникальным и не может быть пустым.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Полное имя пользователя.
     * Это поле не может быть пустым.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * Пароль пользователя.
     * Это поле не может быть пустым.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Роли пользователя.
     * Каждому пользователю может быть присвоено несколько ролей.
     * Роли хранятся в отдельной таблице и извлекаются немедленно (EAGER).
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;
}
