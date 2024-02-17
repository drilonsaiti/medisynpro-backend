package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    private Long appointmentId;
    private LocalDateTime appointmentDate;

    private Long patientId;
    private String patientName;
    private String email;
    private String gender;
    private String address;
    private String contactNumber;
    private LocalDate birthDay;

    private String doctorName;
    private String doctorSpecializations;
    private String doctorImageUrl;

    private Long clinicId;
    private String clinicName;

    private LocalDateTime date;

    private List<String> serviceName;


    private boolean attended;
    private MedicalReportDto report;


}
