package com.example.medisyncpro.model.dto;

import com.example.medisyncpro.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DoctorResultDto {

    private List<Doctor> clinics;
    private int totalElements;

}
