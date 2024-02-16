package com.example.medisyncpro.repository;

import com.example.medisyncpro.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByPatientId(Long id);

    List<Appointment> findAllByDoctorId(Long id);

    List<Appointment> findAllByDateAfterOrderByDateAsc(LocalDateTime time);

    Appointment findByDateAndPatientId(LocalDateTime date, Long patientId);

    Appointment findByPatientId(Long id);
    Appointment findByClinicId(Long id);
    Appointment findByAppointmentId(Long id);
    List<Appointment> findAllByClinicId(Long id);
}
