package com.example.ticket.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
@Data
@AllArgsConstructor
public class Ticket {
    private int id;
    private Route routeId;
    private LocalDateTime dateDepart;
    private int seatNumber;
    private int price;

//    public Ticket(int id, Route route, Date dateDepart, int seatNumber, int price) {
//        this.id = id;
//        this.route = route;
//        this.dateDepart = dateDepart;
//        this.seatNumber = seatNumber;
//        this.price = price;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public Route getRoute() {
//        return route;
//    }
//
//    public void setRoute(Route route) {
//        this.route = route;
//    }
//
//    public Date getDateDepart() {
//        return dateDepart;
//    }
//
//    public void setDateDepart(Date dateDepart) {
//        this.dateDepart = dateDepart;
//    }
//
//    public int getSeatNumber() {
//        return seatNumber;
//    }
//
//    public void setSeatNumber(int seatNumber) {
//        this.seatNumber = seatNumber;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    @Override
//    public String toString() {
//        return "Ticket{" +
//                "id=" + id +
//                ", route=" + route +
//                ", dateDepart=" + dateDepart +
//                ", seatNumber=" + seatNumber +
//                ", price=" + price +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Ticket ticket = (Ticket) o;
//        return id == ticket.id && seatNumber == ticket.seatNumber && price == ticket.price && Objects.equals(route, ticket.route) && Objects.equals(dateDepart, ticket.dateDepart);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, route, dateDepart, seatNumber, price);
//    }
}
