package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceBySpecializationIdDto {

    private Long specializationId;
    private String services;
}
