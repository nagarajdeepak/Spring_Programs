package com.example.movieticket.exception;

public class MovieNotAvailableException extends RuntimeException {
	public MovieNotAvailableException(String message) {
		super(message);
	}
}