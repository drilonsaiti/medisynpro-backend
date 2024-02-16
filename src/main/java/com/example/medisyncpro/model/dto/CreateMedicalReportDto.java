package com.example.medisyncpro.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateMedicalReportDto {

    private Long appointmentId;

    private String medicineName;

    private String disease;

    private LocalDateTime nextAppointmentDate;
    private Integer noOfDays;

}
