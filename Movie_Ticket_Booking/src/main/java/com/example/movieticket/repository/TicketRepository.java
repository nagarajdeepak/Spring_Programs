package com.example.movieticket.repository;

import com.example.movieticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByMovieName(String movieName);

    Optional<Ticket> findByMovieNameAndSeatNumber(String movieName, String seatNumber);

    boolean existsByMovieNameAndSeatNumber(String movieName, String seatNumber);
}