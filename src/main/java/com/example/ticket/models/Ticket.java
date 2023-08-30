package com.example.ticket.models;

import lombok.Data;

import java.util.Date;
@Data
public class Ticket {
    private Route route;
    private Date dateDepart;
    private int seatNumber;
    private int price;
}
