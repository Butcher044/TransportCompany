package com.example.demo.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Утилитный класс для работы с JWT (JSON Web Token).
 * Обрабатывает извлечение данных из токенов, генерацию токенов, а также их валидацию.
 */
@Component
public class JwtUtil {

    private final String SECRET_KEY = "c4F1kM8MwL97dcbSkz82uBzT6TtDkPq3ZbGfv9qCnR3a9AsvBbKj8U5Zg6m48WhP";

    /**
     * Извлекает имя пользователя (subject) из JWT токена.
     *
     * @param token JWT токен.
     * @return имя пользователя (subject) из токена.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает дату истечения срока действия токена.
     *
     * @param token JWT токен.
     * @return дата истечения срока действия токена.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает указанное требуемое значение (claim) из JWT токена.
     *
     * @param token JWT токен.
     * @param claimsResolver функция для извлечения claim из объекта Claims.
     * @param <T> тип возвращаемого значения.
     * @return требуемое значение из claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все claims из JWT токена.
     *
     * @param token JWT токен.
     * @return объект Claims, содержащий все данные токена.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Проверяет, истек ли срок действия JWT токена.
     *
     * @param token JWT токен.
     * @return true, если токен истек, иначе false.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Генерирует новый JWT токен для пользователя.
     *
     * @param userDetails детали пользователя, для которого генерируется токен.
     * @return сгенерированный JWT токен.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().toArray()[0].toString());  // Добавляем роль в claims
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Извлекает роль пользователя из JWT токена.
     *
     * @param token JWT токен.
     * @return роль пользователя, закодированная в токене.
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));  // Извлекаем роль
    }

    /**
     * Создает JWT токен с указанными данными (claims) и именем пользователя.
     *
     * @param claims  дополнительные данные, которые будут закодированы в токен.
     * @param subject имя пользователя (subject), для которого генерируется токен.
     * @return сгенерированный JWT токен.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 часов
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Проверяет, является ли JWT токен действительным.
     * Валидность проверяется на основе имени пользователя и срока действия токена.
     *
     * @param token JWT токен.
     * @param userDetails детали пользователя для валидации.
     * @return true, если токен действителен, иначе false.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
