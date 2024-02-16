package com.example.medisyncpro.service;

import com.example.medisyncpro.model.MedicalReport;
import com.example.medisyncpro.model.dto.CreateMedicalReportDto;
import com.example.medisyncpro.model.dto.MedicalReportDto;
import com.example.medisyncpro.model.dto.MedicalReportResultDto;
import org.springframework.data.domain.PageRequest;

public interface MedicalReportService {

    MedicalReportDto getById(Long id,String authHeader) throws Exception ;

    MedicalReportResultDto getAll(PageRequest page, String nameOrEmail, String byDate,String authHeader) throws Exception ;

    MedicalReport save(CreateMedicalReportDto medicalReport,String authHeader) throws Exception ;

    MedicalReport update(MedicalReport medicalReport,String authHeader) throws Exception;

    void delete(Long id,String authHeader) throws Exception;

    MedicalReportResultDto getMedicalReportByPatient(PageRequest page,String authHeader);
}
