package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.Patient;
import com.example.medisyncpro.model.dto.CreatePatientDto;
import com.example.medisyncpro.model.dto.UpdatePatientDto;
import org.springframework.stereotype.Service;

@Service
public class PatientMapper {

    public Patient createPatient(CreatePatientDto dto) {
        return new Patient(dto.getPatientName(), dto.getGender(), dto.getAddress(),
                dto.getContactNumber(), dto.getEmail(), dto.getBirthDay());
    }

    public Patient updatePatient(Patient old, UpdatePatientDto newP) {
        old.setPatientName(newP.getPatientName());
        old.setGender(newP.getGender());
        old.setAddress(newP.getAddress());
        old.setContactNumber(newP.getContactNumber());
        old.setEmail(newP.getEmail());
        old.setBirthDay(newP.getBirthDay());

        return old;
    }
}
