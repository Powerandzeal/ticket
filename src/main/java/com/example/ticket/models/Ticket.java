package com.example.ticket.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Schema(description = "Сущность Билета")
@Data
@AllArgsConstructor
public class Ticket {
    private int id;

    private Route routeId;

    private LocalDateTime dateDepart;

    private int seatNumber;

    private int price;

    private User owner;

}
