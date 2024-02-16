package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorForClinicDto {

    private Long doctorId;
    private String doctorName;
}
