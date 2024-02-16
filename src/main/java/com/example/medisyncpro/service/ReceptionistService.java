package com.example.medisyncpro.service;

import com.example.medisyncpro.model.Receptionist;
import com.example.medisyncpro.model.dto.AddReceptionistToClinicDto;
import com.example.medisyncpro.model.dto.CreateReceptionistDto;
import com.example.medisyncpro.model.dto.ReceptionistDto;
import com.example.medisyncpro.model.dto.SearchReceptionistDto;

import java.util.List;

public interface ReceptionistService {

    Receptionist getById(Long id);

    ReceptionistDto getByIdDto(Long id);

    List<Receptionist> getAll();

    List<Receptionist> getAllByClinicId(Long clinicId,String authHeader) throws Exception;

    List<SearchReceptionistDto> getAllReceptionistSearch(Long clinicId,String authHeader) throws Exception;

    void addReceptionistToClinic(List<AddReceptionistToClinicDto> dto, Long clinicId,String authHeader) throws Exception;

    Receptionist save(CreateReceptionistDto dto);

    Receptionist update(Receptionist receptionist,String authHeader) throws Exception;

    void delete(Long id,String authHeader) throws Exception;

    void deleteReceptionistFromClinic(Long id,String authHeader) throws Exception;
}

