package com.example.medisyncpro.model.dto;

import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.Specializations;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class ClinicDto {
    private Long clinicId;

    private String clinicName;

    private String address;

    private String email;

    private Set<Specializations> specializations;
    private List<ServiceBySpecializationIdDto> serviceDto;

    private List<Doctor> doctors;

    private String morningHours;
    private String afternoonHours;

}
