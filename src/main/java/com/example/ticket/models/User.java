package com.example.ticket.models;

import com.example.ticket.configuration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
@Schema(description = "Сущность пользователя")
@Data
@AllArgsConstructor
public class User {
    @Schema(description = "Идентификатор")
    private int id;

    @Schema(description = "Полное имя пользователя")
    private String fullName;

    @Schema(description = "Пароль")
    private String password;

    @Schema(description = "Логин")
    private String login;

    @Schema(description = "Роль")
    private Role role;
}
