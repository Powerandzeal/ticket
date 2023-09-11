package com.example.ticket.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Schema(description = "Сущность маршрута")
@Data
@AllArgsConstructor
public class Route {
    private int id;
    private String pointOfDeparture;
    private String destination;
    private Carrier carrier;
    private int tripDuration;

}
