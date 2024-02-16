package com.example.medisyncpro.repository;

import com.example.medisyncpro.model.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {

    Optional<Receptionist> findByEmailAddress(String email);

    Optional<Receptionist> findByReceptionistId(Long id);
}
