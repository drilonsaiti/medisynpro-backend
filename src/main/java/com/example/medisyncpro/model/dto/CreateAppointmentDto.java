package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentDto {
    private Long patientId;
    private Long doctorId;
    private Long clinicId;
    private LocalDateTime date;
    private List<Long> serviceId;
}
