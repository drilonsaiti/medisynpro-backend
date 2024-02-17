package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.Clinic;
import com.example.medisyncpro.model.Receptionist;
import com.example.medisyncpro.model.dto.AddReceptionistToClinicDto;
import com.example.medisyncpro.model.dto.CreateReceptionistDto;
import com.example.medisyncpro.model.dto.ReceptionistDto;
import com.example.medisyncpro.model.dto.SearchReceptionistDto;
import com.example.medisyncpro.model.excp.ClinicException;
import com.example.medisyncpro.model.excp.ReceptionistException;
import com.example.medisyncpro.model.mapper.ReceptionistMapper;
import com.example.medisyncpro.repository.ClinicRepository;
import com.example.medisyncpro.repository.ReceptionistRepository;
import com.example.medisyncpro.service.AuthHeaderService;
import com.example.medisyncpro.service.ReceptionistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ReceptionistRepository receptionistRepository;
    private final ReceptionistMapper receptionistMapper;
    private final ClinicRepository clinicRepository;
    private final AuthHeaderService authHeaderService;

    @Override
    public Receptionist getById(Long id) {
        return receptionistRepository.findById(id)
                .orElseThrow(() -> new ReceptionistException(String.format("Receptionist with id: %d, not found", id)));
    }

    @Override
    public ReceptionistDto getByIdDto(Long id) {
        return receptionistRepository.findById(id).map(receptionist -> {
            Clinic clinic = clinicRepository.findByClinicId(receptionist.getClinicId()).orElseThrow(() -> new ClinicException(String.format("Clinic with id: %d not found", receptionist.getClinicId())));
            String clinicName = clinic != null ? clinic.getClinicName() : "Unemployed";
            return receptionistMapper.getReceptionistById(receptionist, clinicName);
        }).orElse(null);
    }

    @Override
    public List<Receptionist> getAll() {
        try {
            return receptionistRepository.findAll();
        } catch (Exception e) {
            throw new ReceptionistException("Error retrieving all receptionists,try again");
        }
    }

    @Override
    public List<Receptionist> getAllByClinicId(Long clinicId, String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(clinicId, clinicIdAuth)) {
            try {
                return receptionistRepository.findAll().stream()
                        .filter(r -> r.getClinicId() != null && r.getClinicId().equals(clinicIdAuth))
                        .filter(r -> Objects.equals(r.getClinicId(), clinicId)).toList();
            } catch (Exception e) {
                throw new ReceptionistException("Error retrieving all receptionists,try again");
            }
        }
        throw new ReceptionistException("You don't have access");
    }

    @Override
    public List<SearchReceptionistDto> getAllReceptionistSearch(Long clinicId, String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(clinicId, clinicIdAuth)) {
            try {
                return receptionistRepository.findAll().stream()
                        .filter(r -> !Objects.equals(r.getClinicId(), clinicId))
                        .map(r -> new SearchReceptionistDto(r.getEmailAddress(), r.getReceptionistName())).toList();
            } catch (Exception e) {
                throw new ReceptionistException("Error retrieving all receptionists,try again");
            }
        }
        throw new ReceptionistException("You don't have access");
    }

    @Override
    public Receptionist save(CreateReceptionistDto dto) {
        try {
            return receptionistRepository.save(receptionistMapper.createReceptionist(dto));
        } catch (Exception e) {
            throw new ReceptionistException("Error saving receptionist");
        }
    }

    @Override
    public Receptionist update(Receptionist receptionist, String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(receptionist.getClinicId(), clinicIdAuth)) {
            try {
                Receptionist r = this.receptionistRepository.findByReceptionistId(receptionist.getReceptionistId())
                        .orElseThrow(() -> new ReceptionistException(String.format("Receptionist with id: %d, not founded", receptionist.getReceptionistId())));
                r.setReceptionistName(receptionist.getReceptionistName());
                r.setEmailAddress(receptionist.getEmailAddress());
                r.setClinicId(receptionist.getClinicId());
                return receptionistRepository.save(r);
            } catch (Exception e) {
                throw new ReceptionistException("Error updating receptionist", e);
            }
        }
        throw new ReceptionistException("You don't have access");
    }

    @Override
    public void delete(Long id, String authHeader) throws Exception {
        String email = this.authHeaderService.getEmail(authHeader);
        Receptionist r = this.receptionistRepository.findByEmailAddress(email).orElse(null);
        if (r != null && email.equals(r.getEmailAddress())) {
            try {
                receptionistRepository.deleteById(id);
            } catch (Exception e) {
                throw new ReceptionistException("Error deleting receptionist");
            }
        }
    }

    @Override
    public void deleteReceptionistFromClinic(Long id, String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(id, clinicIdAuth)) {
            try {
                Receptionist receptionist = this.receptionistRepository.findByReceptionistId(id).orElseThrow(() -> new ReceptionistException(String.format("Receptionist with id: %d, not founded", id)));
                receptionist.setClinicId(null);
                receptionistRepository.save(receptionist);
            } catch (Exception e) {
                throw new ReceptionistException("Error deleting receptionist from clinic", e);
            }
        }
    }


    @Override
    public void addReceptionistToClinic(List<AddReceptionistToClinicDto> dto, String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);


            try {

                for (AddReceptionistToClinicDto add : dto) {
                    Receptionist receptionist = receptionistRepository.findByEmailAddress(add.getEmail())
                            .orElseThrow(() -> new ReceptionistException(String.format("Doctor with email: %s not found", add.getEmail())));
                    Clinic clinic = clinicRepository.findByClinicId(clinicIdAuth).orElseThrow(() -> new ClinicException(String.format("Clinic with id: %d not found", clinicIdAuth)));


                    receptionist.setClinicId(clinic.getClinicId());
                    receptionistRepository.save(receptionist);

                }

            } catch (Exception e) {
                throw new ReceptionistException("Error adding doctor to clinic: " + e.getMessage());
            }
    }

    @Override
    public ReceptionistDto getReceptionistProfile(String authHeader) throws Exception {
        String email = this.authHeaderService.getEmail(authHeader);
        return this.receptionistRepository.findByEmailAddress(email).map(receptionist -> {
            Clinic clinic = clinicRepository.findByClinicId(receptionist.getClinicId()).orElseThrow(() -> new ClinicException(String.format("Clinic with id: %d not found", receptionist.getClinicId())));
            String clinicName = clinic != null ? clinic.getClinicName() : "Unemployed";
            return receptionistMapper.getReceptionistById(receptionist, clinicName);
        }).orElse(null);
    }
}
