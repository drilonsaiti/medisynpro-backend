package com.example.medisyncpro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "CLINIC")
@NoArgsConstructor
@OnDelete(action = OnDeleteAction.CASCADE)
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinic_id")
    private Long clinicId;

    @Column(name = "clinic_email")
    private String emailAddress;

    @Column(name = "clinic_name")
    private String clinicName;

    @Column(name = "address")
    private String address;

    @Column(name = "doctor_imageUrl")
    private String imageUrl;

    @OneToMany
    private Set<Specializations> specializations;

    @OneToMany(mappedBy = "clinic")
    @JsonIgnore
    private List<Doctor> doctors;

    @OneToMany
    @JsonIgnore
    List<ClinicServices> services;


    @Column(name = "settings_id")

    private Long settingsId;

    // TODO imageurl

    public Clinic(String clinicName, String address) {
        this.clinicName = clinicName;
        this.address = address;
        this.specializations = new HashSet<>();
        this.doctors = new ArrayList<>();
    }

    public Clinic(String emailAddress,String clinicName,String address) {
        this.emailAddress = emailAddress;
        this.clinicName = clinicName;
        this.address = "";
        this.specializations = new HashSet<>();
        this.doctors = new ArrayList<>();
    }
}

