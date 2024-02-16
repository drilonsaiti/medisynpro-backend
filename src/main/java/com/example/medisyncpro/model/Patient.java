package com.example.medisyncpro.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "PATIENT")
@NoArgsConstructor

public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "birthDay")
    private LocalDate birthDay;

    public Patient(String patientName, String gender, String address,
                   String contactNumber, String email, LocalDate birthDay) {
        this.patientName = patientName;
        this.gender = gender;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.birthDay = birthDay;
    }
}