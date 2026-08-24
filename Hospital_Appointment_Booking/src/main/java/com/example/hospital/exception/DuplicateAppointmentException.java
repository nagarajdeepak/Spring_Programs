package com.example.hospital.exception;

public class DuplicateAppointmentException extends RuntimeException {
	
	public DuplicateAppointmentException(String doctorName, String appointmentDate) {
		super("Doctor '"+ doctorName + "' already has an appointment booked on "+appointmentDate);
	}
}