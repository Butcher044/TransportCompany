package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Класс, представляющий маршрут (route) в системе.
 * Содержит информацию о месте отправления, месте прибытия, времени маршрута и создателе.
 */
@Data
@Entity
@Table(name="routs")
public class Route {

    /**
     * Уникальный идентификатор маршрута.
     * Это значение автоматически генерируется в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Место отправления маршрута.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    private String placeOfDeparture;

    /**
     * Место прибытия маршрута.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    private String placeOfArrival;

    /**
     * Идентификатор пользователя, который создал маршрут.
     * Это поле служит для привязки маршрута к конкретному пользователю.
     */
    @Column(nullable = false)
    private Long createdBy;

    /**
     * Время маршрута в минутах.
     * Хранит информацию о продолжительности маршрута.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    private Long routeDuration; // Время маршрута в минутах
}
