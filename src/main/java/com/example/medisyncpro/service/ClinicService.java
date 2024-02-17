package com.example.medisyncpro.service;

import com.example.medisyncpro.model.Clinic;
import com.example.medisyncpro.model.ClinicServices;
import com.example.medisyncpro.model.dto.ClinicDto;
import com.example.medisyncpro.model.dto.ClinicResultDto;
import com.example.medisyncpro.model.dto.UpdateClinicDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ClinicService {

    Clinic getById(Long id);

    ClinicDto getByIdDto(Long id);

    ClinicDto getByIdAuth(String authHeader) throws Exception;
    ClinicDto getMyProfile(String authHeader);

    ClinicResultDto getAll(PageRequest pageable, String specializations, String service, String byDate);

    List<ClinicServices> getClinicServicesById(Long id,String authHeader)throws Exception;

    Clinic save(Clinic clinic);

    void delete(Long id,String authHeader)throws Exception;

    long getTotalClinicCount(String specialization, String status);

    Clinic updateClinic(UpdateClinicDto dto,String authHeader)throws Exception;
}

