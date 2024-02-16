package com.example.medisyncpro.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "APPOINTMENT")
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "clinic_id", nullable = false)
    private Long clinicId;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ElementCollection
    private List<Long> serviceIds = new ArrayList<>();

    @Column(name = "attended", nullable = false)
    private boolean attended;

    public Appointment(Long patientId, Long doctorId, Long clinicId, LocalDateTime date) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.clinicId = clinicId;
        this.date = date;
        this.serviceIds = new ArrayList<>();
        this.attended = false;
    }

    public void setAllServicesIds(List<Long> serviceIds) {
        this.serviceIds.addAll(serviceIds);
    }
}