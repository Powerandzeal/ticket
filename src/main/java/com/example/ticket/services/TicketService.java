package com.example.ticket.services;

import com.example.ticket.DBUtils;
import com.example.ticket.models.Carrier;
import com.example.ticket.models.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
@AllArgsConstructor
public class TicketService {
    private final CrudUtils crudUtils;
    public Set<Ticket> getAllTicket() {
        return null;
    }

    public Ticket createTicket() {
        return null;

    }
    public Carrier updateTicket() {
        return null;
    }
    public Carrier deleteTicket() {
        return null;
    }
}
