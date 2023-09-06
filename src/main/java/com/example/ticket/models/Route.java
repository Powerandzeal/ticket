package com.example.ticket.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Schema(description = "Сущность маршрута")
@Data
@AllArgsConstructor
public class Route {
    @Schema(description = "Идентификатор")
    private int id;
    @Schema(description = "Точка отправления")
    private String pointOfDeparture;
    @Schema(description = "Точка назначения")
    private String destination;
    @Schema(description = "Перевозчик")
    private Carrier carrier;
    @Schema(description = "Продолжительность марщрута в минутах")
    private int tripDuration;

}
