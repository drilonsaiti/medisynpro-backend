package com.example.medisyncpro.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateClinicServicesDto {


    private String serviceName;


    private Integer durationMinutes;


    private BigDecimal price;

    private Long specializationsId;
}
