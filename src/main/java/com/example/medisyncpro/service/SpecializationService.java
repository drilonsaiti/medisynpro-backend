package com.example.medisyncpro.service;

import com.example.medisyncpro.model.Specializations;
import com.example.medisyncpro.model.dto.AddSpecializationToClinicDto;
import com.example.medisyncpro.model.dto.CreateSpecializationDto;

import java.util.List;
import java.util.Set;

public interface SpecializationService {

    Specializations getById(Long id);

    List<Specializations> getAll();

    Specializations save(CreateSpecializationDto dto);

    Specializations update(Specializations specializations);

    Set<Specializations> getSpecializationByClinicId(String authHeader) throws Exception;


    void delete(Long id);

    void addSpecializationToClinic(List<AddSpecializationToClinicDto> dto, String authHeader) throws Exception;

}
