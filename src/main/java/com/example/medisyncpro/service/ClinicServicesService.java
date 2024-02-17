package com.example.medisyncpro.service;

import com.example.medisyncpro.model.ClinicServices;
import com.example.medisyncpro.model.dto.ClinicServicesResultDto;
import com.example.medisyncpro.model.dto.CreateClinicServicesDto;
import com.example.medisyncpro.model.dto.ServiceForClinicsDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ClinicServicesService {

    ClinicServices getById(Long id);

    ClinicServicesResultDto getAll(PageRequest page, String specialization, String sort);

    ClinicServices save(CreateClinicServicesDto clinicServices);

    ClinicServices update(ClinicServices clinicServices);

    void delete(Long id);

    List<ClinicServices> findAllBySpecializationsId(Long id);

    List<ClinicServices> getClinicServiceForClinic();}
