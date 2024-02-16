package com.example.medisyncpro.service;

import com.example.medisyncpro.model.Patient;
import com.example.medisyncpro.model.dto.CreatePatientDto;
import com.example.medisyncpro.model.dto.PatientResultDto;
import com.example.medisyncpro.model.dto.UpdatePatientDto;
import org.springframework.data.domain.PageRequest;

public interface PatientService {

    Patient getById(Long id);
    Patient getPatientProfile(String authHeader);

    PatientResultDto getAll(PageRequest pageable, String nameOrEmail,String authHeader) throws Exception;

    Patient save(CreatePatientDto patient) throws Exception;

    Patient update(UpdatePatientDto patient,String authHeader) throws Exception;

    void delete(Long id,String authHeader) throws Exception;
}
