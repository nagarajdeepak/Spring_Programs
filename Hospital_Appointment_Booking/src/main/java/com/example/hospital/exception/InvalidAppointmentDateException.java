package com.example.hospital.exception;

public class InvalidAppointmentDateException extends RuntimeException{
	
	public InvalidAppointmentDateException(String message) {
		super(message);
	}
}