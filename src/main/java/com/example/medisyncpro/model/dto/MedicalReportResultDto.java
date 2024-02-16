package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MedicalReportResultDto {
    private List<MedicalReportDto> medicalReport;
    private int totalElements;
}
