package com.example.medisyncpro.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "CLINIC_SCHEDULE")
@NoArgsConstructor

public class ClinicSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "clinic_id", nullable = false)
    private Long clinicId;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_booked", nullable = false)
    private Boolean isBooked;

    public ClinicSchedule(Long doctorId, Long clinicId, Date date, LocalDateTime startTime, LocalDateTime endTime) {
        this.doctorId = doctorId;
        this.clinicId = clinicId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBooked = false;
    }
}
