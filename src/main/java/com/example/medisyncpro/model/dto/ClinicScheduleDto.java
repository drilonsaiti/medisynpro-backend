package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ClinicScheduleDto {
    private Long id;
    private Date date;
    private String doctorName;
    private String timeSlot;
    private Boolean isBooked;

}