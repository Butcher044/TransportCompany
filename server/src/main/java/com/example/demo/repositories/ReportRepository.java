package com.example.demo.repositories;

import com.example.demo.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Report}.
 * Содержит методы для взаимодействия с таблицей отчетов в базе данных.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // Этот интерфейс наследует все стандартные методы JpaRepository, такие как save, findById, delete и т.д.
}
