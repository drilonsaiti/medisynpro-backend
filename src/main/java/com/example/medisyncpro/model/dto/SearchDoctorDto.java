package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchDoctorDto {
    private String email;
    private String name;
}
