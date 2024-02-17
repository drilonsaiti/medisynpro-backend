package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.Clinic;
import com.example.medisyncpro.model.Specializations;
import com.example.medisyncpro.model.dto.AddSpecializationToClinicDto;
import com.example.medisyncpro.model.dto.CreateSpecializationDto;
import com.example.medisyncpro.model.excp.ClinicException;
import com.example.medisyncpro.model.excp.ReceptionistException;
import com.example.medisyncpro.model.excp.SpecializationException;
import com.example.medisyncpro.model.mapper.SpecializationMapper;
import com.example.medisyncpro.repository.ClinicRepository;
import com.example.medisyncpro.repository.SpecializationRepository;
import com.example.medisyncpro.service.AuthHeaderService;
import com.example.medisyncpro.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final SpecializationMapper specializationMapper;
    private final AuthHeaderService authHeaderService;
    private final ClinicRepository clinicRepository;
    @Override
    public Specializations getById(Long id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new SpecializationException("Specialization not found with id: " + id));
    }

    @Override
    public List<Specializations> getAll() {
        try {
            return specializationRepository.findAll();
        } catch (Exception e) {
            throw new SpecializationException("Error retrieving all specializations", e);
        }
    }

    @Override
    public Set<Specializations> getSpecializationByClinicId(String authHeader) throws Exception {
        Long clinicId = authHeaderService.getClinicId(authHeader);
        Clinic c = this.clinicRepository.findByClinicId(clinicId).orElse(null);
        try {
            return c.getSpecializations();
        } catch (Exception e) {
            throw new SpecializationException("Error retrieving all specializations", e);
        }
    }

    @Override
    public Specializations save(CreateSpecializationDto dto) {
        try {
            return specializationRepository.save(specializationMapper.createSpecialization(dto));
        } catch (Exception e) {
            throw new SpecializationException("Error saving specialization", e);
        }
    }

    @Override
    public Specializations update(Specializations specializations) {
        try {
            Specializations existingSpecialization = getById(specializations.getSpecializationId());
            existingSpecialization.setSpecializationName(specializations.getSpecializationName());
            return specializationRepository.save(existingSpecialization);
        } catch (Exception e) {
            throw new SpecializationException("Error updating specialization", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            specializationRepository.deleteById(id);
        } catch (Exception e) {
            throw new SpecializationException("Error deleting specialization", e);
        }
    }

    @Override
    public void addSpecializationToClinic(List<AddSpecializationToClinicDto> dto, String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);
        try {
            for (AddSpecializationToClinicDto add : dto) {
                Specializations s = this.getById(add.getSpecializationId());
                Clinic clinic = clinicRepository.findByClinicId(clinicIdAuth).orElseThrow(() -> new ClinicException(String.format("Clinic with id: %d not found", clinicIdAuth)));

                clinic.getSpecializations().add(s);
                clinicRepository.save(clinic);

            }

        } catch (Exception e) {
            throw new SpecializationException("Error adding doctor to clinic: " + e.getMessage());
        }
    }
}
