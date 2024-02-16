package com.example.medisyncpro.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorSettingsDto {

    private Long doctorId;
    private String doctorName;
}
