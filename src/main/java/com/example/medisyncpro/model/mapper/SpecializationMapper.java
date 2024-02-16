package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.Specializations;
import com.example.medisyncpro.model.dto.CreateSpecializationDto;
import org.springframework.stereotype.Service;

@Service
public class SpecializationMapper {

    public Specializations createSpecialization(CreateSpecializationDto dto) {
        return new Specializations(dto.getSpecializationName());
    }
}
