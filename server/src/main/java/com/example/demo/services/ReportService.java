package com.example.demo.services;

import com.example.demo.models.Report;
import com.example.demo.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с сущностью {@link Report}.
 * Обрабатывает логику получения, создания и удаления отчетов.
 */
@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    /**
     * Получает список всех отчетов.
     *
     * @return Список всех отчетов.
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Создает новый отчет.
     *
     * @param report Объект {@link Report}, который нужно создать.
     * @return Созданный объект {@link Report}.
     */
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    /**
     * Получает отчет по его ID.
     *
     * @param id ID отчета.
     * @return Объект {@link Report} с указанным ID.
     * @throws RuntimeException если отчет с таким ID не найден.
     */
    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отчет с id не найден: " + id));
    }

    /**
     * Удаляет отчет по его ID.
     *
     * @param id ID отчета, который нужно удалить.
     */
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
