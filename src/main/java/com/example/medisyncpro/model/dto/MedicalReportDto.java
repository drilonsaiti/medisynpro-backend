package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalReportDto {
    private Long reportId;
    private String disease;
    private String medicine;
    private LocalDateTime nextAppointment;
    private LocalDateTime reportDate;

    private Long patientId;
    private String patientName;
    private String patientEmail;

    private String doctorName;
    private Long doctorId;
    private String doctorEmail;
    private LocalDateTime appointmentDate;
    private List<ServiceDto> services;
    private int totalPrice;
}
