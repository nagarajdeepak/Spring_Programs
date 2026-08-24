package com.example.movieticket.controller;

import com.example.movieticket.entity.Ticket;
import com.example.movieticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> bookTicket(@Valid @RequestBody Ticket ticket) {
        Ticket booked = ticketService.bookTicket(ticket);
        return new ResponseEntity<>(booked, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/movie/{movieName}")
    public ResponseEntity<List<Ticket>> getTicketsByMovie(@PathVariable String movieName) {
        return ResponseEntity.ok(ticketService.getTicketsByMovie(movieName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.ok("Ticket with id " + id + " cancelled successfully");
    }
    
    @PostMapping("/bulk")
    public ResponseEntity<List<Ticket>> bookTickets(@Valid @RequestBody List<Ticket> tickets) {
        List<Ticket> booked = ticketService.bookTickets(tickets);
        return new ResponseEntity<>(booked, HttpStatus.CREATED);
    }
}