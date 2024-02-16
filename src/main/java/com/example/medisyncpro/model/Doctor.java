package com.example.medisyncpro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "DOCTOR")
@NoArgsConstructor

public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    @Column(name = "doctor_email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specializations specialization;

    @Column(name = "education")
    private String education;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private List<MedicalReport> medicalReports;

    public Doctor(String doctorName, Specializations specialization, String education, String email, Clinic clinic) {
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.education = education;
        this.email = email;
        this.clinic = clinic;
        this.medicalReports = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + doctorId +
                ", name='" + doctorName + '\'' +
                // other fields excluding those causing circular references
                '}';
    }
}