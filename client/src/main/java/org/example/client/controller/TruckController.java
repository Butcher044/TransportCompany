package org.example.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.example.client.service.TruckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

/**
 * Контроллер для управления функциональностью грузовиков.
 * Предоставляет методы для просмотра, добавления и удаления грузовиков,
 * а также проверки ролей пользователей.
 */
@Controller
@RequestMapping("/trucks")
public class TruckController {

    private final TruckService truckService;

    /**
     * Конструктор для создания экземпляра TruckController.
     *
     * @param truckService сервис для работы с грузовиками
     */
    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    /**
     * Возвращает список всех грузовиков.
     *
     * @param model   объект Model для передачи данных в представление
     * @param session объект HttpSession для получения данных сеанса
     * @return имя шаблона для отображения списка грузовиков
     */
    @GetMapping("/list")
    public String getAllTrucks(Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }

            var trucks = truckService.getAllTrucks(token);
            model.addAttribute("trucks", trucks);

            String role = extractUserRoleFromToken(token);

            if ("ROLE_DRIVER".equals(role)) {
                return "truck/trucks_list_driver";
            } else if ("ROLE_ADMIN".equals(role) || "ROLE_MANAGER".equals(role)) {
                return "truck/trucks_list";
            } else {
                model.addAttribute("error", "Недостаточно прав для просмотра грузовиков.");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка получения списка грузовиков: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Отображает форму добавления нового грузовика.
     *
     * @param model объект Model для передачи данных в представление
     * @return имя шаблона для формы добавления грузовика
     */
    @GetMapping("/add")
    public String showAddTruckForm(Model model) {
        return "truck/truck_add";
    }

    /**
     * Добавляет новый грузовик на основе предоставленных данных.
     *
     * @param params  параметры запроса, содержащие данные о грузовике
     * @param model   объект Model для передачи данных в представление
     * @param session объект HttpSession для получения данных сеанса
     * @return имя шаблона для отображения результата операции
     */
    @PostMapping("/add")
    public String addTruck(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            truckService.addTruck(params, token);
            model.addAttribute("success", "Грузовик успешно добавлен!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка добавления грузовика: " + e.getMessage());
        }
        return "truck/truck_add";
    }

    /**
     * Удаляет грузовик по его идентификатору.
     *
     * @param id      идентификатор грузовика
     * @param session объект HttpSession для получения данных сеанса
     * @param model   объект Model для передачи данных в представление
     * @return перенаправление на страницу со списком грузовиков или имя шаблона при ошибке
     */
    @PostMapping("/delete/{id}")
    public String deleteTruck(@PathVariable Long id, HttpSession session, Model model) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            truckService.deleteTruck(id, token);
            return "redirect:/trucks/list";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка удаления грузовика: " + e.getMessage());
            return "truck/trucks_list";
        }
    }

    /**
     * Извлекает роль пользователя из JWT токена.
     *
     * @param token строка JWT токена
     * @return роль пользователя, извлеченная из токена
     * @throws RuntimeException если роль не удалось извлечь из токена
     */
    private String extractUserRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(parts[1]));
            Map<String, Object> claims = new ObjectMapper().readValue(payload, Map.class);
            return (String) claims.get("role");
        } catch (Exception e) {
            throw new RuntimeException("Не удалось извлечь роль из токена: " + e.getMessage());
        }
    }
}
