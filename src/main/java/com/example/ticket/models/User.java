package com.example.ticket.models;

import lombok.Data;

@Data
public class User {
    private int id;
    private String login;
    private String password;
    private String fullName;
}
