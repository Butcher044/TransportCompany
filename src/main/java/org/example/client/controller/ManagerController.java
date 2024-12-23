package org.example.client.controller;

import jakarta.servlet.http.HttpSession;
import org.example.client.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для управления.
 */
@Controller
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    /**
     * Конструктор контроллера.
     *
     * @param managerService сервис для работы с данными менеджеров.
     */
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    /**
     * Обрабатывает GET-запрос для отображения страницы профиля менеджера.
     *
     * @return имя шаблона страницы профиля менеджера.
     */
    @GetMapping("/profile")
    public String managerProfilePage() {
        return "manager/manager_profile";
    }

    /**
     * Обрабатывает POST-запрос для создания профиля менеджера.
     *
     * @param params параметры профиля менеджера, переданные через запрос.
     * @param model объект {@link Model} для передачи данных на страницу.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @return имя шаблона страницы профиля менеджера.
     */
    @PostMapping("/profile")
    public String createManagerProfile(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            managerService.createManagerProfile(params, token);
            model.addAttribute("success", "Профиль менеджера успешно создан!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка создания профиля: " + e.getMessage());
        }
        return "manager/manager_profile";
    }

    /**
     * Обрабатывает GET-запрос для получения списка всех менеджеров.
     *
     * @param model объект {@link Model} для передачи данных на страницу.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @return имя шаблона страницы со списком менеджеров.
     */
    @GetMapping("/list")
    public String getAllManagers(Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            var managers = managerService.getAllManagers(token);
            model.addAttribute("managers", managers);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка получения списка менеджеров: " + e.getMessage());
        }
        return "manager/managers_list";
    }

    /**
     * Обрабатывает POST-запрос для установки зарплаты менеджеру.
     *
     * @param id идентификатор менеджера.
     * @param salary новая зарплата менеджера.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @param model объект {@link Model} для передачи данных на страницу.
     * @return строка с перенаправлением на список менеджеров.
     */
    @PostMapping("/salary/{id}")
    public String setManagerSalary(@PathVariable Long id, @RequestParam double salary, HttpSession session, Model model) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            managerService.setManagerSalary(id, salary, token);
            model.addAttribute("success", "Зарплата успешно обновлена!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления зарплаты: " + e.getMessage());
        }
        return "redirect:/managers/list";
    }

    /**
     * Обрабатывает POST-запрос для удаления менеджера.
     *
     * @param id идентификатор менеджера.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @param model объект {@link Model} для передачи данных на страницу.
     * @return строка с перенаправлением на список менеджеров или текущую страницу в случае ошибки.
     */
    @PostMapping("/delete/{id}")
    public String deleteManager(@PathVariable Long id, HttpSession session, Model model) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            managerService.deleteManager(id, token);
            return "redirect:/managers/list";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка удаления менеджера: " + e.getMessage());
            return "manager/managers_list";
        }
    }
}
