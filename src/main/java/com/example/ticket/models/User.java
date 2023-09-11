package com.example.ticket.models;

import com.example.ticket.configurations.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Сущность пользователя")
@Data
@AllArgsConstructor
public class User {
    private int id;

    private String fullName;

    private String password;

    private String login;

    private Role role;
}
