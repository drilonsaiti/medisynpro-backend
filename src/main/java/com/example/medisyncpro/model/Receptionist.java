package com.example.medisyncpro.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "RECEPTIONIST")
@NoArgsConstructor
public class Receptionist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receptionist_id")
    private Long receptionistId;

    @Column(name = "receptionist_name")
    private String receptionistName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "clinic_id")
    private Long clinicId;

    public Receptionist(String receptionistName, String emailAddress, Long clinicId) {
        this.receptionistName = receptionistName;
        this.emailAddress = emailAddress;
        this.clinicId = clinicId;
    }
}

