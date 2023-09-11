package com.example.ticket.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Dto билета")
@Data
@AllArgsConstructor
public class TicketDto {
    int id;
    @Schema(description = "Точка отправления")
    String pointOfDeparture;
    @Schema(description = "Точка назначения")
    String destination;
    @Schema(description = "Название перевозчика ")
    String nameCarrier;
    @Schema(description = "Время пути")
    int tripDuration;
    @Schema(description = "Номер места")
    int seatNumber;
    @Schema(description = "Стоимость билета")
    int price;
    @Schema(description = "Дата оптравления")
    private LocalDateTime dateDepart;

}
