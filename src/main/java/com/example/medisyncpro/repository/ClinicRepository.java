package com.example.medisyncpro.repository;

import com.example.medisyncpro.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    Optional<Clinic> findByClinicId(Long id);
    Clinic findByEmailAddress(String email);
}
