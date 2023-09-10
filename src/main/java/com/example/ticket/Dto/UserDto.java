package com.example.ticket.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    @Schema(description = "Полное имя пользователя")
    private String fullName;

    @Schema(description = "Пароль")
    private String password;

    @Schema(description = "Логин")
    private String login;
}
