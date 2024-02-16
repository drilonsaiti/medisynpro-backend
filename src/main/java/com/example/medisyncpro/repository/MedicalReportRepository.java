package com.example.medisyncpro.repository;

import com.example.medisyncpro.model.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalReportRepository extends JpaRepository<MedicalReport, Long> {

    Optional<MedicalReport> findByAppointmentId(Long id);

    Optional<MedicalReport> findByReportId(Long id);
    List<MedicalReport> findAllByClinicId(Long id);

    List<MedicalReport> findAllByPatientId(Long id);
}
