package com.example.medisyncpro.model.dto;

import com.example.medisyncpro.model.Specializations;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateClinicDto {

    private Long clinicId;

    private String clinicName;

    private String address;

    private Set<Specializations> specializations;
    private List<ServiceForClinicsDto> serviceDto;

    private List<DoctorForClinicDto> doctors;

}
