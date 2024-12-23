package org.example.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для управления отображением страниц в зависимости от роли пользователя.
 */
@Controller
public class RoleController {

    /**
     * Обрабатывает запрос для отображения панели администратора.
     *
     * @return Имя шаблона для страницы панели администратора.
     */
    @GetMapping("/dashboard/admin_dashboard")
    public String adminDashboard() {
        return "dashboard/admin_dashboard";  // Название вашего шаблона
    }

    /**
     * Обрабатывает запрос для отображения панели менеджера.
     *
     * @return Имя шаблона для страницы панели менеджера.
     */
    @GetMapping("/dashboard/manager_dashboard")
    public String managerDashboard() {
        return "dashboard/manager_dashboard";  // Название вашего шаблона
    }

    /**
     * Обрабатывает запрос для отображения панели водителя.
     *
     * @return Имя шаблона для страницы панели водителя.
     */
    @GetMapping("/dashboard/driver_dashboard")
    public String driverDashboard() {
        return "dashboard/driver_dashboard";  // Название вашего шаблона
    }

    /**
     * Обрабатывает запрос для отображения страницы "Об авторе".
     *
     * @return Имя шаблона для страницы "Об авторе".
     */
    @GetMapping("/about")
    public String aboutPage() {
        return "dashboard/about";
    }
}
