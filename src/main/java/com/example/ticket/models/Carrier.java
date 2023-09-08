package com.example.ticket.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@Schema(description = "Сущность перевозчика")
@Data
@AllArgsConstructor
public class Carrier {
    @Schema(description = "Идентификатор")
    private int id;
    @Schema(description = "Название перевозчика")
    private String name;
    @Schema(description = "Номер телефона")
    private int phoneNumber;
}
