package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Модель данных для водителя.
 * Содержит информацию о водителе, такую как ФИО, дата рождения, номер телефона, лицензия, медицинский сертификат и т.д.
 */
@Data
@Entity
@Table(name = "drivers")
public class Driver {

    /**
     * Уникальный идентификатор водителя.
     * Генерируется автоматически при сохранении записи в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Полное имя водителя.
     * Обязательное поле.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * Дата рождения водителя.
     * Отображается в формате "yyyy-MM-dd".
     * Обязательное поле.
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    /**
     * Номер телефона водителя.
     * Уникальное и обязательное поле.
     */
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    /**
     * Адрес проживания водителя.
     * Обязательное поле.
     */
    @Column(nullable = false)
    private String residenceAddress;

    /**
     * Номер водительского удостоверения.
     * Уникальное и обязательное поле.
     */
    @Column(unique = true, nullable = false)
    private String drivingLicense;

    /**
     * Образование водителя.
     * Обязательное поле.
     */
    @Column(nullable = false)
    private String education;

    /**
     * Срок действия медицинского сертификата водителя.
     * Отображается в формате "yyyy-MM-dd".
     * Обязательное поле.
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validityPeriodOfTheMedicalCertificate;

    /**
     * Стаж вождения водителя в годах.
     * Обязательное поле.
     */
    @Column(nullable = false)
    private int drivingExperience;

    /**
     * Уровень квалификации водителя.
     * Обязательное поле.
     */
    @Column(nullable = false)
    private String driverLevel;

    /**
     * Зарплата водителя.
     * Это поле может быть пустым (например, если зарплата не указана).
     */
    private double salary;

    /**
     * Ссылка на идентификатор пользователя в системе.
     * Обязательное поле, которое связывает водителя с конкретным пользователем.
     */
    @Column(nullable = false)
    private Long userId; // Ссылка на UserModel
}
