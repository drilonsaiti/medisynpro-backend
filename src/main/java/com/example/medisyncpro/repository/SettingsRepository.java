package com.example.medisyncpro.repository;

import com.example.medisyncpro.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

    Optional<Settings> findById(Long id);

    Optional<Settings> findByClinicId(Long id);
}
