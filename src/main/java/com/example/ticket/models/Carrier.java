package com.example.ticket.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@Schema(description = "Сущность перевозчика")
@Data
@AllArgsConstructor
public class Carrier {
    private int id;
    private String name;
    private int phoneNumber;
}
