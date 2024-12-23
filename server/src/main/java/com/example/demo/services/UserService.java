package com.example.demo.services;

import com.example.demo.models.UserModel;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с пользователями.
 * Включает методы для сохранения пользователя, поиска пользователя по имени и проверки существования пользователя.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Сохраняет пользователя в базе данных.
     *
     * @param user Пользователь, которого необходимо сохранить.
     * @return Сохраненный объект {@link UserModel}.
     */
    public UserModel saveUser(UserModel user) {
        return userRepository.save(user);
    }


    /**
     * Проверяет, существует ли пользователь с данным именем пользователя.
     *
     * @param username Логин пользователя.
     * @return true, если пользователь с таким логином существует, иначе false.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
