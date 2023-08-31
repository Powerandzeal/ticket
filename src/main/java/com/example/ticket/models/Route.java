package com.example.ticket.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class Route {
    private int id;
    private String pointOfDeparture;
    private String destination;
    private Carrier carrier;
    private int tripDuration;

}
