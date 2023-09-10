package com.example.ticket.Dto;

import com.example.ticket.models.Carrier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteDTO {

    @Schema(description = "Точка отправления")
    private String pointOfDeparture;
    @Schema(description = "Точка назначения")
    private String destination;
    @Schema(description = "Id Перевозчик")
    private int carrierId;
    @Schema(description = "Продолжительность марщрута в минутах")
    private int tripDuration;
}
