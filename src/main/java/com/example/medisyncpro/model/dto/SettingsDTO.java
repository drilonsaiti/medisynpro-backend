package com.example.medisyncpro.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class SettingsDTO {
    private Long id;
    private Long clinicId;
    private LocalTime morningStartTime;
    private LocalTime morningEndTime;
    private LocalTime afternoonStartTime;
    private LocalTime afternoonEndTime;
    private int appointmentDurationMinutes;
    private int daysToGenerate;
    private List<DoctorSettingsDto> morningDoctors;
    private List<DoctorSettingsDto> afternoonDoctors;
    // Other fields as needed

    // Constructor, getters, setters
}
