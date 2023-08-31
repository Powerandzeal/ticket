package com.example.ticket.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String fullName;
    private String password;
    private String login;
}
