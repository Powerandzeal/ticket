package com.example.ticket.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@AllArgsConstructor
public class Carrier {
    private int id;
    private String name;
    private int phoneNumber;
}
