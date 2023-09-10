package com.example.ticket.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class TicketCreateDTO {
    @Schema(description = "Точка отправления")
    int id;
    @Schema(description = "Точка назначения")
    int routeId;
    @Schema(description = "Дата отправления")
    private LocalDateTime dateDepart;
    @Schema(description = "Время пути")
    int seatNumber;
    @Schema(description = "Номер места")
    int price;
    int owner;

}
