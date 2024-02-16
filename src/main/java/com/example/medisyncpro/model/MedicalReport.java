package com.example.medisyncpro.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "MEDICAL_REPORT")
@NoArgsConstructor

public class MedicalReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "clinic_id")
    private Long clinicId;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "disease")
    private String disease;

    @Column(name = "next_appointment_date")
    private LocalDateTime nextAppointmentDate;

    @Column(name = "no_of_days")
    private Integer noOfDays;

    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;

    @Column(name = "report_date")
    private LocalDateTime reportDate;


    public MedicalReport(Long patientId,Long clinicId, Doctor doctor, String medicineName, String disease, LocalDateTime nextAppointmentDate, Integer noOfDays, LocalDateTime appointmentDate, Long appointmentId) {
        this.patientId = patientId;
        this.clinicId = clinicId;
        this.doctor = doctor;
        this.medicineName = medicineName;
        this.disease = disease;
        this.appointmentId = appointmentId;
        this.nextAppointmentDate = nextAppointmentDate;
        this.noOfDays = noOfDays;
        this.appointmentDate = appointmentDate;
        this.reportDate = LocalDateTime.now();
    }
}
