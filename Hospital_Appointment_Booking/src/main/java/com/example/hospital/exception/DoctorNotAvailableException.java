package com.example.hospital.exception;

public class DoctorNotAvailableException extends RuntimeException{
	
	public DoctorNotAvailableException(String doctorName) {
		super("Doctor '"+ doctorName +"is not available for appointments");
	}
}