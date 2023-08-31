package com.example.ticket.controllers;

import com.example.ticket.models.Carrier;
import com.example.ticket.models.Ticket;
import com.example.ticket.models.User;
import com.example.ticket.services.CrudUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketController {

    private final CrudUtils crudUtils;
    @GetMapping("getAllTickets")
    public List<Ticket> getAllTicket() {
        return crudUtils.getAllTicket();
    }
    @GetMapping("getAllTicketsByPage")
    public List<Ticket> getAllTicketWithPagination(
            @RequestParam (required = false,defaultValue = "0") int page,
            @RequestParam (required = false,defaultValue = "10")int size
    ) {
        return crudUtils.getAllTicket();
    }
    @PatchMapping("/buyTicket")
    public Ticket buyTicket(int ticketId) {
        return null;
    }
//    @GetMapping("getTickets")
//    public String getTicket() {
//        return "Нижний Новгород-гороховец.21.09.2023, место 2 ";
//    }
//    @PostMapping("createTicket")
//    public Ticket createTicket() {
//        return null;
//    }
//    @PatchMapping()
//    public Ticket updateTicket() {
//        return null;
//    }
//    @DeleteMapping()
//    public Ticket deleteTicket() {
//        return null;
//    }
}
