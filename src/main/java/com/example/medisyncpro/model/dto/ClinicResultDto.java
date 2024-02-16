package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClinicResultDto {

    private List<ClinicDto> clinics;
    private int totalElements;

}
