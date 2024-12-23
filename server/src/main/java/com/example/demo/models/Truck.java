package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Класс, представляющий грузовик (truck) в системе.
 * Содержит информацию о бренде, модели, годе выпуска, регистрационном номере,
 * вместимости, объеме кузова и номере гаража.
 */
@Data
@Entity
@Table(name = "trucks")
public class Truck {

    /**
     * Уникальный идентификатор грузовика.
     * Это значение автоматически генерируется в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Бренд грузовика.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    private String brand;

    /**
     * Модель грузовика.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    private String modelTruck;

    /**
     * Год выпуска грузовика.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate yearOfRelease;

    /**
     * Регистрационный номер грузовика.
     * Должен быть уникальным и не может быть пустым.
     */
    @Column(unique = true, nullable = false)
    private String registrationMark;

    /**
     * Вместимость грузовика в тоннах.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    private int capacity;

    /**
     * Объем кузова грузовика в кубических метрах.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    private int bodyVolume;

    /**
     * Номер гаража, где хранится грузовик.
     * Не может быть пустым.
     */
    @Column(nullable = false)
    private int garageNumber;

    /**
     * Идентификатор пользователя, который создал запись о грузовике.
     * Это поле служит для привязки грузовика к конкретному пользователю.
     */
    @Column(nullable = false)
    private Long createdBy;
}
