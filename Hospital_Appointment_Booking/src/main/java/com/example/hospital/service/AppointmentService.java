package com.example.hospital.service;

import com.example.hospital.exception.*;
import com.example.hospital.model.Appointment;
import com.example.hospital.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Doctors currently not taking new appointments (demo data for DoctorNotAvailableException)
    private static final Set<String> UNAVAILABLE_DOCTORS = Set.of("dr. house", "dr. strange");

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorName) {
        List<Appointment> appointments = appointmentRepository.findByDoctorNameIgnoreCase(doctorName);
        if (appointments.isEmpty()) {
            throw new InvalidRequestException("No appointments found for doctor: " + doctorName);
        }
        return appointments;
    }

    public Appointment bookAppointment(Appointment appointment) {
        validateAppointment(appointment);

        if (UNAVAILABLE_DOCTORS.contains(appointment.getDoctorName().trim().toLowerCase())) {
            throw new DoctorNotAvailableException(appointment.getDoctorName());
        }

        List<Appointment> existing = appointmentRepository.findByDoctorNameIgnoreCaseAndAppointmentDate(
                appointment.getDoctorName(), appointment.getAppointmentDate());
        if (!existing.isEmpty()) {
            throw new DuplicateAppointmentException(
                    appointment.getDoctorName(), appointment.getAppointmentDate().toString());
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment updatedDetails) {
        Appointment appointment = getAppointmentById(id);
        validateAppointment(updatedDetails);

        appointment.setPatientName(updatedDetails.getPatientName());
        appointment.setDoctorName(updatedDetails.getDoctorName());
        appointment.setDisease(updatedDetails.getDisease());
        appointment.setAppointmentDate(updatedDetails.getAppointmentDate());

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointmentRepository.delete(appointment);
    }

    private void validateAppointment(Appointment appointment) {
        if (appointment.getAppointmentDate() != null
                && appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new InvalidAppointmentDateException(
                    "Appointment date " + appointment.getAppointmentDate() + " cannot be in the past");
        }
    }
}
