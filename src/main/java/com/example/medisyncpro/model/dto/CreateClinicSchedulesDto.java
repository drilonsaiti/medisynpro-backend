package com.example.medisyncpro.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class CreateClinicSchedulesDto {
    private Long doctorId;

    private Long clinicId;

    private Date date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isBooked;
}
