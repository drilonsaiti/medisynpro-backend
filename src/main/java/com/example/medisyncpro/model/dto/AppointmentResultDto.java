package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AppointmentResultDto {

    private List<AppointmentDto> appointments;
    private int totalElements;
}
