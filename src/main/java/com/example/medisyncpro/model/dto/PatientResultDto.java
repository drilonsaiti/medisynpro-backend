package com.example.medisyncpro.model.dto;

import com.example.medisyncpro.model.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PatientResultDto {
    private List<Patient> patients;
    private int totalElements;
}
