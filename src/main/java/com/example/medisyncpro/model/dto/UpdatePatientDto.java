package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientDto {
    private Long patientId;
    private String patientName;

    private String gender;

    private String address;

    private String contactNumber;

    private String email;

    private LocalDate birthDay;
}
