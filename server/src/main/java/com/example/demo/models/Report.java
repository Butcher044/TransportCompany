package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Модель данных для отчёта.
 * Содержит информацию о созданном отчёте, такую как описание, дата отчёта и кто его создал.
 */
@Data
@Entity
@Table(name="reports")
public class Report {

    /**
     * Уникальный идентификатор отчёта.
     * Генерируется автоматически при сохранении записи в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Описание отчёта.
     * Обязательное поле, которое содержит информацию о содержании отчёта.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Дата, к которой относится отчёт.
     * Отображается в формате "yyyy-MM-dd".
     * Обязательное поле.
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateReport;

    /**
     * Идентификатор пользователя, который создал отчёт.
     * Обязательное поле, указывающее на пользователя, создавшего данный отчёт.
     */
    @Column(nullable = false)
    private Long createdBy;
}
