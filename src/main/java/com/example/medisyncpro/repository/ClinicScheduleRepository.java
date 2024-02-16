package com.example.medisyncpro.repository;

import com.example.medisyncpro.model.ClinicSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ClinicScheduleRepository extends JpaRepository<ClinicSchedule, Long> {

    ClinicSchedule findClinicScheduleByClinicIdAndStartTime(Long id, LocalDateTime startTime);

    ClinicSchedule findClinicScheduleByClinicId(Long id);

    List<ClinicSchedule> findAllByClinicId(Long id);

    List<ClinicSchedule> findAllByClinicIdAndDate(Long id, Date date);
    List<ClinicSchedule> findAllByDoctorId(Long id);

    ClinicSchedule findByScheduleId(Long id);
}

