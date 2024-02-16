package com.example.medisyncpro.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateDoctorDto {

    private String doctorName;


    private Long specializationId;

    private String education;

    private Long clinicId;

    private String doctorEmail;

    public CreateDoctorDto(String doctorEmail,String doctorName) {
        this.doctorEmail = doctorEmail;
        this.doctorName = doctorName;
        this.specializationId = null;
        this.education = "";
        this.clinicId = null;
    }
}
