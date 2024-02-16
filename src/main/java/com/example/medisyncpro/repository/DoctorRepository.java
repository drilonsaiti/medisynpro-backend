package com.example.medisyncpro.repository;

import com.example.medisyncpro.model.Clinic;
import com.example.medisyncpro.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findAllByClinic(Clinic clinic);

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByDoctorId(Long id);
}