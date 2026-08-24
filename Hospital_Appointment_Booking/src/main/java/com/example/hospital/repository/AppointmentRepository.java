package com.example.hospital.repository;

import com.example.hospital.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorNameIgnoreCase(String doctorName);

    List<Appointment> findByPatientNameIgnoreCase(String patientName);

    List<Appointment> findByDoctorNameIgnoreCaseAndAppointmentDate(String doctorName, LocalDate appointmentDate);
}
