package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientDto {
    private String patientName;

    private String gender;

    private String address;

    private String contactNumber;

    private String email;

    private LocalDate birthDay;

    public CreatePatientDto(String email,String patientName) {
        this.patientName = patientName;
        this.email = email;
        this.gender = "";
        this.address = "";
        this.contactNumber = "";
        this.birthDay = null;
    }
}
