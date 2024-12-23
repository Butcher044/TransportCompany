package com.example.demo.controllers;

import com.example.demo.JWT.JwtUtil;
import com.example.demo.models.Role;
import com.example.demo.models.UserModel;
import com.example.demo.services.UserDetailsServiceImpl;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 * Обрабатывает запросы на регистрацию, вход в систему и извлечение ролей из токенов.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * Регистрация нового пользователя. Проверяет наличие ролей, их допустимость и уникальность имени пользователя.
     * После проверки сохраняет пользователя в базе данных.
     *
     * @param user объект {@link UserModel}, содержащий данные пользователя для регистрации.
     * @return {@link ResponseEntity} с сообщением о результате операции.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserModel user) {
        Set<Role> validRoles = Set.of(Role.ADMIN, Role.DRIVER, Role.MANAGER);

        // Проверка наличия ролей у пользователя
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return ResponseEntity.badRequest().body("Роли не указаны или пусты.");
        }

        // Проверка на допустимость указанных ролей
        for (Role role : user.getRoles()) {
            if (!validRoles.contains(role)) {
                return ResponseEntity.badRequest().body("Недопустимая роль: " + role + ".");
            }
        }

        // Проверка на уникальность имени пользователя
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует.");
        }

        // Шифрование пароля перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован!");
    }

    /**
     * Извлекает роль пользователя из переданного JWT токена.
     *
     * @param authHeader заголовок Authorization, содержащий JWT токен.
     * @return строка с ролью пользователя или сообщение об ошибке.
     */
    @GetMapping("/getRoleFromToken")
    public String getRoleFromToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String role = jwtUtil.extractRole(token);  // Извлекаем роль из токена
                return role;
            } catch (Exception e) {
                return "Error extracting role from token";  // Обработка ошибок при извлечении роли
            }
        }
        return "Authorization header missing or invalid";  // Ошибка, если заголовок отсутствует или некорректен
    }

    /**
     * Выполняет аутентификацию пользователя и генерирует JWT токен при успешной аутентификации.
     *
     * @param user объект {@link UserModel}, содержащий данные пользователя для входа.
     * @return {@link ResponseEntity} с JWT токеном или сообщением об ошибке.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserModel user) {
        try {
            // Аутентификация пользователя
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            // Генерация JWT токена
            String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));
            return ResponseEntity.ok(token);  // Возвращаем токен при успешной аутентификации
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Неверный логин или пароль.");  // Ошибка при аутентификации
        }
    }

}
