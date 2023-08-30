package com.example.ticket.models;

import lombok.Data;

import java.util.Date;
@Data
public class Route {
    private String pointOfDeparture;
    private String destination;
    private Carrier carrier;
    private int tripDuration;
}
