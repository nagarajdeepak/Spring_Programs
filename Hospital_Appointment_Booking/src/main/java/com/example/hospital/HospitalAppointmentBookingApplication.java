package com.example.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalAppointmentBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalAppointmentBookingApplication.class, args);
	}

}


// Add dependencies: Spring Web, Spring Data JPA, Validation, H2 Database
// Spring Data JPA : Is a part of the Spring ecosystem that simplifies database access in Java applications using the Java Persistence API (JPA)
// Validation : Is used to check whether the data received from the client is correct before processing or saving it to the database.
// H2 Database : Is a lightweight, fast, Java-based relational database commonly used for development, testing, and learning Spring Boot/JPA

//{ "patientName": "Rohan Das", "doctorName": "Dr. Priya Nair", "disease": "Fever", "appointmentDate": "2026-09-10" }
// { "patientName": "Meera Pillai", "doctorName": "Dr. Sameer Khan", "disease": "Joint Pain", "appointmentDate": "2026-09-11" }
// { "patientName": "Arjun Menon", "doctorName": "Dr. Anjali Mehta", "disease": "High Blood Pressure", "appointmentDate": "2026-09-12" }
// { "patientName": "Kavya Nambiar", "doctorName": "Dr. Arjun Rao", "disease": "Migraine", "appointmentDate": "2026-09-13"}
// { "patientName": "Siddharth Rao", "doctorName": "Dr. Priya Nair", "disease": "Sprained Ankle", "appointmentDate": "2026-09-14" }
// { "patientName": "Ananya Bhat", "doctorName": "Dr. Sameer Khan", "disease": "Allergic Rhinitis", "appointmentDate": "2026-09-15" }
// { "patientName": "Nikhil Chawla", "doctorName": "Dr. Anjali Mehta", "disease": "Diabetes Checkup", "appointmentDate": "2026-09-16" }
// { "patientName": "Ishita Sen", "doctorName": "Dr. Arjun Rao", "disease": "Throat Infection", "appointmentDate": "2026-09-17" }
// { "patientName": "Farhan Ahmed", "doctorName": "Dr. Priya Nair", "disease": "Eye Irritation", "appointmentDate": "2026-09-18" }