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
    @Schema(description = "Идентификатор")
    private int id;

    @Schema(description = "Идентификатор маршрута")
    private Route routeId;

    @Schema(description = "Дата отправления")
    private LocalDateTime dateDepart;

    @Schema(description = "Номер места")
    private int seatNumber;

    @Schema(description = "Цена")
    private int price;

    @Schema(description = "Идентификатор владельца")
    private User owner;

//    public Ticket(int id, Route route, Date dateDepart, int seatNumber, int phoneNumber) {
//        this.id = id;
//        this.route = route;
//        this.dateDepart = dateDepart;
//        this.seatNumber = seatNumber;
//        this.phoneNumber = phoneNumber;
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
//    public int getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(int phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    @Override
//    public String toString() {
//        return "Ticket{" +
//                "id=" + id +
//                ", route=" + route +
//                ", dateDepart=" + dateDepart +
//                ", seatNumber=" + seatNumber +
//                ", phoneNumber=" + phoneNumber +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Ticket ticket = (Ticket) o;
//        return id == ticket.id && seatNumber == ticket.seatNumber && phoneNumber == ticket.phoneNumber && Objects.equals(route, ticket.route) && Objects.equals(dateDepart, ticket.dateDepart);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, route, dateDepart, seatNumber, phoneNumber);
//    }
}
