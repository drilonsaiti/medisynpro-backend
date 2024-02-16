package com.example.medisyncpro.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "SPECIALIZATION")
@NoArgsConstructor
public class Specializations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "specialization_name", nullable = false)
    private String specializationName;

    public Specializations(String specializationName) {
        this.specializationName = specializationName;
    }
}