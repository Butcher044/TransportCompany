package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Модель данных для менеджера.
 * Содержит информацию о менеджере, такую как ФИО, дата рождения, номер телефона, зарплата и другие данные.
 */
@Data
@Entity
@Table(name="managers")
public class Manager {

    /**
     * Уникальный идентификатор менеджера.
     * Генерируется автоматически при сохранении записи в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Полное имя менеджера.
     * Обязательное поле.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * Дата рождения менеджера.
     * Отображается в формате "yyyy-MM-dd".
     * Обязательное поле.
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    /**
     * Номер телефона менеджера.
     * Уникальное и обязательное поле.
     */
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    /**
     * Адрес проживания менеджера.
     * Обязательное поле.
     */
    @Column(nullable = false)
    private String residenceAddress;

    /**
     * Зарплата менеджера.
     * Это поле может быть пустым (например, если зарплата не указана).
     */
    private double salary;

    /**
     * Ссылка на идентификатор пользователя в системе.
     * Обязательное поле, которое связывает менеджера с конкретным пользователем.
     */
    @Column(nullable = false)
    private Long userId; // Ссылка на UserModel
}
