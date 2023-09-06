package com.example.ticket.security;

import com.example.ticket.models.User;
import com.example.ticket.services.UserService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 * Реализация интерфейса {@link UserDetailsService} для аутентификации пользователей.
 * Этот сервис загружает информацию о пользователе из {@link UserService}.
 */
@Schema(description = "Реализация UserDetailsService ")
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    /**
     * Загружает информацию о пользователе по его имени пользователя (логину).
     *
     * @param username имя пользователя (логин)
     * @return объект UserDetails, представляющий информацию о пользователе
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByLogin(username).orElseThrow();
        System.out.println(user);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        return userDetails;
    }


}
