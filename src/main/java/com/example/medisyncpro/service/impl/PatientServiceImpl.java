package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.Patient;
import com.example.medisyncpro.model.dto.CreatePatientDto;
import com.example.medisyncpro.model.dto.PatientResultDto;
import com.example.medisyncpro.model.dto.UpdatePatientDto;
import com.example.medisyncpro.model.excp.PatientException;
import com.example.medisyncpro.model.mapper.PatientMapper;
import com.example.medisyncpro.repository.MedicalReportRepository;
import com.example.medisyncpro.repository.PatientRepository;
import com.example.medisyncpro.service.AuthHeaderService;
import com.example.medisyncpro.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AuthHeaderService authHeaderService;
    private final MedicalReportRepository medicalReportRepository;

    @Override
    public Patient getById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientException("Patient not found with ID: " + id));
    }

    @Override
    public PatientResultDto getAll(PageRequest pageable, String nameOrEmail,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        try {
            List<Patient> patients = medicalReportRepository.findAllByClinicId(clinicId).stream().map(report -> {
                    Patient p = this.getById(report.getPatientId());
                    return p;
                    }).toList();
                   patients = patients.stream()
                    .filter(patient ->
                    (((nameOrEmail.equals("all") || patient.getPatientName().toLowerCase().contains(nameOrEmail.toLowerCase())
                            || patient.getEmail().toLowerCase().contains(nameOrEmail.toLowerCase()))))
            ).toList();

            int totalElements = patients.size();

            return new PatientResultDto(patients.stream()
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize()).toList(), totalElements);
        } catch (Exception e) {
            throw new PatientException("Error retrieving all patients");
        }
    }

    @Override
    public Patient save(CreatePatientDto patient){
        try {
            if (patientRepository.existsPatientByEmail(patient.getEmail())) {
                throw new PatientException("The patient already exists with this email");
            }
            return patientRepository.save(patientMapper.createPatient(patient));
        } catch (Exception e) {
            throw new PatientException("Error saving patient", e);
        }
    }

    @Override
    public Patient update(UpdatePatientDto patient,String authHeader) throws Exception {
       Patient patientAuth = this.patientRepository.findByEmail(this.authHeaderService.getEmail(authHeader));

       if (patientAuth.getPatientId() == patient.getPatientId()) {
           try {
               Patient p = this.getById(patient.getPatientId());
               return patientRepository.save(patientMapper.updatePatient(p, patient));
           } catch (Exception e) {
               throw new PatientException("Error updating patient", e);
           }
       }
       throw new PatientException("You don't have access");
    }

    @Override
    public void delete(Long id,String authHeader) throws Exception {
        Patient patientAuth = this.patientRepository.findByEmail(this.authHeaderService.getEmail(authHeader));
        if (patientAuth.getPatientId() == id){
        try {
            patientRepository.deleteById(id);
        } catch (Exception e) {
            throw new PatientException("Error deleting patient", e);
        }
        }
        throw new PatientException("You don't have access");
    }

    @Override
    public Patient getPatientProfile(String authHeader) {
        String email = authHeaderService.getEmail(authHeader);
        return patientRepository.findByEmail(email);
    }
}
