package com.example.movieticket.service;

import com.example.movieticket.entity.Ticket;
import com.example.movieticket.exception.*;
import com.example.movieticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Ticket bookTicket(Ticket ticket) {
        if (ticket.getTicketPrice() <= 0) {
            throw new InvalidTicketPriceException("Ticket price must be positive");
        }

        boolean seatTaken = ticketRepository.existsByMovieNameAndSeatNumber(
                ticket.getMovieName(), ticket.getSeatNumber());

        if (seatTaken) {
            throw new SeatAlreadyBookedException(
                    "Seat " + ticket.getSeatNumber() + " is already booked for " + ticket.getMovieName());
        }

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
    }

    public List<Ticket> getTicketsByMovie(String movieName) {
        List<Ticket> tickets = ticketRepository.findByMovieName(movieName);
        if (tickets.isEmpty()) {
            throw new MovieNotAvailableException("No tickets found for movie: " + movieName);
        }
        return tickets;
    }

    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket existing = getTicketById(id);

        // Prevent duplicate seat assignment when updating seat/movie
        boolean isSameSeat = existing.getMovieName().equals(updatedTicket.getMovieName())
                && existing.getSeatNumber().equals(updatedTicket.getSeatNumber());

        if (!isSameSeat && ticketRepository.existsByMovieNameAndSeatNumber(
                updatedTicket.getMovieName(), updatedTicket.getSeatNumber())) {
            throw new DuplicateBookingException(
                    "Seat " + updatedTicket.getSeatNumber() + " is already booked for this movie");
        }

        existing.setMovieName(updatedTicket.getMovieName());
        existing.setCustomerName(updatedTicket.getCustomerName());
        existing.setSeatNumber(updatedTicket.getSeatNumber());
        existing.setTicketPrice(updatedTicket.getTicketPrice());

        return ticketRepository.save(existing);
    }

    public void cancelTicket(Long id) {
        Ticket ticket = getTicketById(id);
        ticketRepository.delete(ticket);
    }
    
    public List<Ticket> bookTickets(List<Ticket> tickets) {
        return tickets.stream()
                .map(this::bookTicket)
                .toList();
    }
}