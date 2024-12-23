package org.example.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.example.client.service.RouteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

/**
 * Контроллер для управления маршрутами.
 */
@Controller
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    /**
     * Конструктор для инициализации RouteService.
     *
     * @param routeService Сервис для управления маршрутами.
     */
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * Обрабатывает запрос для получения списка маршрутов.
     *
     * @param model   Объект модели для передачи данных в представление.
     * @param session HTTP-сессия для получения JWT токена.
     * @return Имя шаблона для отображения списка маршрутов.
     */
    @GetMapping("/list")
    public String getAllRoutes(Model model, HttpSession session) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }

            // Получение списка маршрутов
            var routes = routeService.getAllRoutes(token);
            model.addAttribute("routes", routes);

            // Декодируем JWT токен, чтобы получить роли пользователя
            String role = extractUserRoleFromToken(token);

            // Проверяем роли и возвращаем соответствующий шаблон
            if ("ROLE_DRIVER".equals(role)) {
                return "route/routes_list_driver";
            } else if ("ROLE_ADMIN".equals(role) || "ROLE_MANAGER".equals(role)) {
                return "route/routes_list";
            } else {
                model.addAttribute("error", "Недостаточно прав для просмотра маршрутов.");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка получения списка маршрутов: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Обрабатывает запрос для отображения формы добавления маршрута.
     *
     * @param model Объект модели для передачи данных в представление.
     * @return Имя шаблона для формы добавления маршрута.
     */
    @GetMapping("/add")
    public String showAddRouteForm(Model model) {
        return "route/route_add"; // Показывает форму добавления маршрута с картой
    }

    /**
     * Обрабатывает запрос для добавления нового маршрута.
     *
     * @param params  Параметры маршрута из запроса.
     * @param model   Объект модели для передачи данных в представление.
     * @param session HTTP-сессия для получения JWT токена.
     * @return Имя шаблона для формы добавления маршрута или перенаправление.
     */
    @PostMapping("/add")
    public String addRoute(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        try {
            // Извлекаем данные маршрута
            String startLatitude = params.get("startLatitude");
            String startLongitude = params.get("startLongitude");
            String endLatitude = params.get("endLatitude");
            String endLongitude = params.get("endLongitude");
            String placeOfDeparture = params.get("placeOfDeparture");
            String placeOfArrival = params.get("placeOfArrival");
            String routeDuration = params.get("routeDuration");

            // Формируем Map для передачи в сервис
            Map<String, String> routeData = Map.of(
                    "placeOfDeparture", placeOfDeparture,
                    "placeOfArrival", placeOfArrival,
                    "routeDuration", routeDuration
            );

            // Передаем данные в сервис
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }

            routeService.addRoute(routeData, token, startLatitude, startLongitude, endLatitude, endLongitude);

            model.addAttribute("success", "Маршрут успешно добавлен!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка добавления маршрута: " + e.getMessage());
        }
        return "route/route_add";
    }

    /**
     * Обрабатывает запрос для удаления маршрута по его идентификатору.
     *
     * @param id      Идентификатор маршрута для удаления.
     * @param session HTTP-сессия для получения JWT токена.
     * @param model   Объект модели для передачи данных в представление.
     * @return Перенаправление на список маршрутов или имя шаблона в случае ошибки.
     */
    @PostMapping("/delete/{id}")
    public String deleteRoute(@PathVariable Long id, HttpSession session, Model model) {
        try {
            String token = (String) session.getAttribute("jwtToken");
            if (token == null) {
                model.addAttribute("error", "Необходимо выполнить вход.");
                return "auth/login";
            }
            routeService.deleteRoute(id, token);
            return "redirect:/routes/list";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка удаления маршрута: " + e.getMessage());
            return "route/routes_list";
        }
    }

    /**
     * Извлекает роль пользователя из JWT токена.
     *
     * @param token JWT токен.
     * @return Роль пользователя, извлеченная из токена.
     * @throws RuntimeException Если роль не удалось извлечь из токена.
     */
    private String extractUserRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\."); // JWT состоит из 3 частей: заголовок, полезная нагрузка, подпись
            String payload = new String(Base64.getDecoder().decode(parts[1])); // Декодируем payload
            Map<String, Object> claims = new ObjectMapper().readValue(payload, Map.class); // Парсим JSON
            return (String) claims.get("role"); // Извлекаем роль из claim "role"
        } catch (Exception e) {
            throw new RuntimeException("Не удалось извлечь роль из токена: " + e.getMessage());
        }
    }
}
