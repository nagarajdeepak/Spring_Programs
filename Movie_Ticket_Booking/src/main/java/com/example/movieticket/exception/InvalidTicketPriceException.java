package com.example.movieticket.exception;

public class InvalidTicketPriceException extends RuntimeException {
	public InvalidTicketPriceException(String message) {
		super(message);
	}
}