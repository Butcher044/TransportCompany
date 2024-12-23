package org.example.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Map;

/**
 * Контроллер для обработки возврата пользователя на соответствующую панель в зависимости от роли.
 */
@Controller
@RequestMapping("/button")
public class ButtonBackController {

    /**
     * Обрабатывает запрос на возврат пользователя к панели, соответствующей его роли.
     *
     * @param session объект {@link HttpSession}, из которого извлекается JWT токен пользователя.
     * @return строка с перенаправлением на соответствующую панель пользователя или на страницу входа.
     */
    @GetMapping("/back")
    public String handleBack(HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null) {
            return "redirect:/auth/login";
        }
        try {
            String role = extractUserRoleFromToken(token);

            switch (role) {
                case "ROLE_ADMIN":
                    return "redirect:/dashboard/admin_dashboard";
                case "ROLE_MANAGER":
                    return "redirect:/dashboard/manager_dashboard";
                case "ROLE_DRIVER":
                    return "redirect:/dashboard/driver_dashboard";
                default:
                    return "redirect:/auth/login";
            }
        } catch (Exception e) {
            return "redirect:/auth/login";
        }
    }

    /**
     * Извлекает роль пользователя из JWT токена.
     *
     * @param token JWT токен, из которого извлекается информация о роли.
     * @return строка, содержащая роль пользователя (например, "ROLE_ADMIN").
     * @throws RuntimeException если извлечение роли из токена завершилось ошибкой.
     */
    private String extractUserRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\."); // JWT состоит из 3 частей
            String payload = new String(Base64.getDecoder().decode(parts[1])); // Декодируем payload
            Map<String, Object> claims = new ObjectMapper().readValue(payload, Map.class); // Парсим JSON
            return (String) claims.get("role"); // Извлекаем роль
        } catch (Exception e) {
            throw new RuntimeException("Не удалось извлечь роль из токена: " + e.getMessage());
        }
    }

}
