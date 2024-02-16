package com.example.medisyncpro.model.dto;

import com.example.medisyncpro.model.ClinicServices;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClinicServicesResultDto {
    private List<ClinicServices> services;
    private int totalElements;
}
