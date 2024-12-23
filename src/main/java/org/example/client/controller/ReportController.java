package org.example.client.controller;

import jakarta.servlet.http.HttpSession;
import org.example.client.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для управления отчетами.
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * Конструктор контроллера.
     *
     * @param reportService сервис для работы с отчетами.
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Обрабатывает GET-запрос для получения списка всех отчетов.
     *
     * @param model объект {@link Model} для передачи данных на страницу.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @return имя шаблона страницы со списком отчетов.
     */
    @GetMapping("/list")
    public String getAllReports(Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            var reports = reportService.getAllReports(token);
            model.addAttribute("reports", reports);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка получения списка отчетов: " + e.getMessage());
        }
        return "report/reports_list";
    }

    /**
     * Обрабатывает GET-запрос для отображения формы добавления нового отчета.
     *
     * @param model объект {@link Model} для передачи данных на страницу.
     * @return имя шаблона страницы для добавления отчета.
     */
    @GetMapping("/add")
    public String showAddReportForm(Model model) {
        return "report/report_add";
    }

    /**
     * Обрабатывает POST-запрос для добавления нового отчета.
     *
     * @param params параметры отчета, переданные через запрос.
     * @param model объект {@link Model} для передачи данных на страницу.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @return имя шаблона страницы для добавления отчета.
     */
    @PostMapping("/add")
    public String addReport(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            reportService.addReport(params, token);
            model.addAttribute("success", "Отчет успешно добавлен!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка добавления отчета: " + e.getMessage());
        }
        return "report/report_add";
    }

    /**
     * Обрабатывает POST-запрос для удаления отчета.
     *
     * @param id идентификатор отчета.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @param model объект {@link Model} для передачи данных на страницу.
     * @return строка с перенаправлением на список отчетов или текущую страницу в случае ошибки.
     */
    @PostMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id, HttpSession session, Model model) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            reportService.deleteReport(id, token);
            return "redirect:/reports/list";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка удаления отчета: " + e.getMessage());
            return "report/reports_list";
        }
    }
}
