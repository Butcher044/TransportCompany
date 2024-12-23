package com.example.demo.controllers;

import com.example.demo.config.UserDetailsImpl;
import com.example.demo.models.Report;
import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления отчетами.
 * Обрабатывает запросы для получения, добавления, удаления отчетов и получения отчета по ID.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Получение списка всех отчетов.
     * Доступно для пользователей с ролями ROLE_ADMIN, ROLE_MANAGER и ROLE_DRIVER.
     *
     * @return {@link ResponseEntity} с списком отчетов и HTTP статусом OK.
     */
    @GetMapping("/allReports")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DRIVER')")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    /**
     * Добавление нового отчета.
     * Доступно для пользователей с ролями ROLE_ADMIN, ROLE_MANAGER и ROLE_DRIVER.
     * Устанавливает ID текущего пользователя как владельца отчета.
     *
     * @param report объект {@link Report}, который содержит данные для добавления нового отчета.
     * @return {@link ResponseEntity} с сохраненным отчетом и HTTP статусом CREATED.
     */
    @PostMapping("/addReport")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DRIVER')")
    public ResponseEntity<Report> addReport(@RequestBody Report report) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        report.setCreatedBy(userDetails.getUser().getId()); // Устанавливаем владельца записи

        Report savedReport = reportService.createReport(report);
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }

    /**
     * Удаление отчета по ID.
     * Доступно для пользователей с ролями ROLE_ADMIN, ROLE_MANAGER и ROLE_DRIVER.
     *
     * @param id ID отчета, который необходимо удалить.
     * @return {@link ResponseEntity} с HTTP статусом NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DRIVER')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Получение отчета по ID.
     * Доступно для пользователей с ролями ROLE_ADMIN, ROLE_MANAGER и ROLE_DRIVER.
     *
     * @param id ID отчета, который необходимо получить.
     * @return {@link ResponseEntity} с отчетом и HTTP статусом OK.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_DRIVER')")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report report = reportService.getReportById(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
