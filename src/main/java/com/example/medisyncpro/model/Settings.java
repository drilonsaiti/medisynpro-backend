package com.example.medisyncpro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinic_id")
    private Long clinicId;

    @Column(name = "morning_start_time")
    private LocalTime morningStartTime;

    @Column(name = "morning_end_time")
    private LocalTime morningEndTime;

    @Column(name = "afternoon_start_time")
    private LocalTime afternoonStartTime;

    @Column(name = "afternoon_end_time")
    private LocalTime afternoonEndTime;

    @Column(name = "appointment_duration_minutes")
    private int appointmentDurationMinutes;

    @Column(name = "days_to_generate")
    private int daysToGenerate;

    @ManyToMany
    @JoinTable(
            name = "settings_morning_doctors",
            joinColumns = @JoinColumn(name = "settings_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @JsonIgnore
    private List<Doctor> morningDoctors;

    @ManyToMany
    @JoinTable(
            name = "settings_afternoon_doctors",
            joinColumns = @JoinColumn(name = "settings_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @JsonIgnore
    private List<Doctor> afternoonDoctors;

    public Settings() {
        morningDoctors = new ArrayList<>();
        afternoonDoctors = new ArrayList<>();
    }
}

