package com.example.movieticket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ticketId;
	
	@NotBlank(message = "Movie name is required")
	private String movieName;
	
	@NotBlank(message = "Customer name cannot be empty")
	private String customerName;
	
	@NotBlank(message = "Seat number cannot be empty")
	private String seatNumber;
	
	@Positive(message = "Ticket price must be positive")
	private double ticketPrice;
}