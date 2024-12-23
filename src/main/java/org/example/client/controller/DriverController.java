package org.example.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.example.client.service.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для управления.
 */
@Controller
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    /**
     * Конструктор контроллера.
     *
     * @param driverService сервис для работы с данными водителей.
     */
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * Обрабатывает GET-запрос для отображения страницы профиля водителя.
     *
     * @return имя шаблона страницы профиля водителя.
     */
    @GetMapping("/profile")
    public String driverProfilePage() {
        return "driver/driver_profile";
    }

    /**
     * Обрабатывает POST-запрос для создания профиля водителя.
     *
     * @param params параметры профиля водителя, переданные через запрос.
     * @param model  объект {@link Model} для передачи данных на страницу.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @return имя шаблона страницы профиля водителя.
     */
    @PostMapping("/profile")
    public String createDriverProfile(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            driverService.createDriverProfile(params, token);
            model.addAttribute("success", "Профиль водителя успешно создан!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка создания профиля: " + e.getMessage());
        }
        return "driver/driver_profile";
    }

    /**
     * Обрабатывает GET-запрос для получения списка всех водителей.
     *
     * @param model  объект {@link Model} для передачи данных на страницу.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @return имя шаблона страницы со списком водителей.
     */
    @GetMapping("/list")
    public String getAllDrivers(Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            var drivers = driverService.getAllDrivers(token);
            model.addAttribute("drivers", drivers);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка получения списка водителей: " + e.getMessage());
        }
        return "driver/drivers_list";
    }

    /**
     * Обрабатывает GET-запрос для получения информации о конкретном водителе.
     *
     * @param id идентификатор водителя.
     * @param model объект {@link Model} для передачи данных на страницу.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @return имя шаблона страницы с информацией о водителе.
     */
    @GetMapping("/{id}")
    public String getDriverDetails(@PathVariable Long id, Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            var driver = driverService.getDriverById(id, token);
            model.addAttribute("driver", driver);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка получения информации о водителе: " + e.getMessage());
        }
        return "driver/driver_details";
    }

    /**
     * Обрабатывает POST-запрос для установки зарплаты водителю.
     *
     * @param id     идентификатор водителя.
     * @param salary новая зарплата водителя.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @param model объект {@link Model} для передачи данных на страницу.
     * @return строка с перенаправлением на страницу деталей водителя.
     */
    @PostMapping("/salary/{id}")
    public String setDriverSalary(@PathVariable Long id, @RequestParam double salary, HttpSession session, Model model) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            driverService.setDriverSalary(id, salary, token);
            model.addAttribute("success", "Зарплата успешно обновлена!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления зарплаты: " + e.getMessage());
        }
        return "redirect:/drivers/" + id;
    }

    /**
     * Обрабатывает POST-запрос для удаления водителя.
     *
     * @param id идентификатор водителя.
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен.
     * @param model объект {@link Model} для передачи данных на страницу.
     * @return строка с перенаправлением на список водителей или на текущую страницу в случае ошибки.
     */
    @PostMapping("/delete/{id}")
    public String deleteDriver(@PathVariable Long id, HttpSession session, Model model) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            driverService.deleteDriver(id, token);
            return "redirect:/drivers/list";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка удаления водителя: " + e.getMessage());
            return "driver/drivers_list";
        }
    }
}
