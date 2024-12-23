package org.example.client.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Контроллер для управления процессами аутентификации и регистрации пользователей.
 */
@Controller
public class AuthController {

    private final String BACKEND_URL = "http://localhost:8081/auth";

    /**
     * Обрабатывает запрос на получение страницы входа в систему.
     *
     * @return имя HTML-шаблона страницы входа.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    /**
     * Обрабатывает запрос на аутентификацию пользователя.
     * Проверяет учетные данные пользователя, получает токен и перенаправляет на страницу в зависимости от роли.
     *
     * @param username имя пользователя, введенное в форме входа.
     * @param password пароль пользователя, введенный в форме входа.
     * @param model объект {@link Model} для передачи данных в представление.
     * @param session объект {@link HttpSession} для сохранения токена JWT.
     * @return строка с перенаправлением на соответствующую страницу или имя HTML-шаблона при ошибке.
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Создаем JSON-запрос
            String requestJson = """
            {
                "username": "%s",
                "password": "%s"
            }
            """.formatted(username, password);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

            // Отправляем POST-запрос на бэкенд для получения токена
            ResponseEntity<String> response = restTemplate.exchange(
                    BACKEND_URL + "/login",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Сохраняем токен в сессии
            String token = response.getBody();
            session.setAttribute("jwtToken", token);

            // Создаем новый заголовок с токеном для запроса роли
            HttpHeaders headersWithToken = new HttpHeaders();
            headersWithToken.set("Authorization", "Bearer " + token);

            // Отправляем запрос на получение роли
            ResponseEntity<String> roleResponse = restTemplate.exchange(
                    BACKEND_URL + "/getRoleFromToken",
                    HttpMethod.GET,
                    new HttpEntity<>(headersWithToken),
                    String.class
            );

            String role = roleResponse.getBody();

            // Редиректим на страницу в зависимости от роли
            if ("ROLE_ADMIN".equals(role)) {
                return "redirect:/dashboard/admin_dashboard";  // Перенаправление на панель администратора
            } else if ("ROLE_MANAGER".equals(role)) {
                return "redirect:/dashboard/manager_dashboard";
            } else if ("ROLE_DRIVER".equals(role)) {
                return "redirect:/dashboard/driver_dashboard"; // Перенаправление на панель водителя
            } else {
                model.addAttribute("error", "Неизвестная роль");
                return "auth/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка входа: " + e.getMessage());
            return "auth/login";
        }
    }

    /**
     * Обрабатывает запрос на получение страницы регистрации.
     *
     * @return имя HTML-шаблона страницы регистрации.
     */
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    /**
     * Обрабатывает запрос на регистрацию нового пользователя.
     * Отправляет данные пользователя на бэкенд для создания учетной записи.
     *
     * @param username имя пользователя, введенное в форме регистрации.
     * @param fullName полное имя пользователя, введенное в форме регистрации.
     * @param password пароль пользователя, введенный в форме регистрации.
     * @param role роль пользователя (например, "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_DRIVER").
     * @param model объект {@link Model} для передачи данных в представление.
     * @return имя HTML-шаблона при успешной регистрации или в случае ошибки.
     */
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String fullName,
                           @RequestParam String password, @RequestParam String role,
                           Model model) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Создаем JSON-запрос
            String requestJson = """
                {
                    "username": "%s",
                    "fullName": "%s",
                    "password": "%s",
                    "roles": ["%s"]
                }
            """.formatted(username, fullName, password, role);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

            // Отправляем POST-запрос на бэкенд для регистрации
            restTemplate.exchange(
                    BACKEND_URL + "/register",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            model.addAttribute("success", "Регистрация успешна!");
            return "auth/register";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "auth/register";
        }
    }
}
