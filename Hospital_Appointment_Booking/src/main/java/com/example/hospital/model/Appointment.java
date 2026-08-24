package com.example.hospital.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "appointments")
public class Appointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long appointmentId;
	
	@NotBlank(message = "Patient name is required")
	@Column(nullable = false)
	private String patientName;
	
	@NotBlank(message = "Doctor name is required")
	@Column(nullable = false)
	private String doctorName;
	
	@NotBlank(message = "Disease field cannot be empty")
	@Column(nullable = false)
	private String disease;
	
	@NotNull(message = "Appointment data is required")
	@FutureOrPresent(message = "Appointment data cannot be in the past")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(nullable = false)
	private LocalDate appointmentDate;
	
	public Appointment() {
	}
	
	public Appointment(Long appiontmentId, String patientName, String doctorName, String disease, LocalDate appointmentDate) {
		this.appointmentId = appiontmentId;
		this.patientName = patientName;
		this.doctorName = doctorName;
		this.disease = disease;
		this.appointmentDate = appointmentDate;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
}