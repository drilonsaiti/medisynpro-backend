package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceForClinicsDto {

    private Long serviceId;
    private String serviceName;
}
