package com.example.demo.services;

import com.example.demo.config.UserDetailsImpl;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link UserDetailsService} для работы с пользовательскими данными.
 * Обрабатывает загрузку пользователя по имени пользователя для использования в аутентификации.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Загружает пользователя по имени пользователя.
     * Используется для аутентификации пользователя в процессе входа в систему.
     *
     * @param username Имя пользователя (логин), по которому загружается пользователь.
     * @return Объект {@link UserDetails}, который содержит данные пользователя.
     * @throws UsernameNotFoundException Если пользователь с таким логином не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином не найден: " + username));
        return new UserDetailsImpl(user);
    }
}
