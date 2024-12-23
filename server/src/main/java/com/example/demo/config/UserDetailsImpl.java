package com.example.demo.config;

import com.example.demo.models.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Реализация интерфейса {@link UserDetails} для предоставления информации о пользователе,
 * включая его роли и данные аутентификации.
 */
public class UserDetailsImpl implements UserDetails {

    private final UserModel user;

    /**
     * Конструктор для инициализации {@link UserDetailsImpl} с объектом {@link UserModel}.
     *
     * @param user объект модели пользователя, содержащий данные о пользователе.
     */
    public UserDetailsImpl(UserModel user) {
        this.user = user;
    }

    /**
     * Возвращает объект {@link UserModel}, ассоциированный с этим {@link UserDetails}.
     *
     * @return объект {@link UserModel}, содержащий информацию о пользователе.
     */
    public UserModel getUser() {
        return user;
    }

    /**
     * Возвращает коллекцию ролей пользователя в виде объектов {@link GrantedAuthority}.
     * Каждая роль будет преобразована в {@link SimpleGrantedAuthority} с префиксом "ROLE_".
     *
     * @return коллекция авторизационных прав пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Возвращает имя пользователя (логин).
     *
     * @return имя пользователя (логин).
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Указывает, истек ли срок действия аккаунта пользователя.
     * В данной реализации возвращается {@code true}, что означает, что аккаунт не истек.
     *
     * @return {@code true}, если аккаунт не истек, иначе {@code false}.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Указывает, заблокирован ли аккаунт пользователя.
     * В данной реализации возвращается {@code true}, что означает, что аккаунт не заблокирован.
     *
     * @return {@code true}, если аккаунт не заблокирован, иначе {@code false}.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Указывает, истек ли срок действия учетных данных пользователя.
     * В данной реализации возвращается {@code true}, что означает, что учетные данные не истекли.
     *
     * @return {@code true}, если учетные данные не истекли, иначе {@code false}.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Указывает, активен ли аккаунт пользователя.
     * В данной реализации всегда возвращается {@code true}, что означает, что аккаунт активен.
     *
     * @return {@code true}, если аккаунт активен, иначе {@code false}.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
