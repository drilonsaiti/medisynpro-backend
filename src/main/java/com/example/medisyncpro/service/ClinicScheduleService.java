package com.example.medisyncpro.service;

import com.example.medisyncpro.model.ClinicSchedule;
import com.example.medisyncpro.model.dto.ClinicScheduleResultDto;
import com.example.medisyncpro.model.dto.CreateClinicSchedulesDto;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;
import java.util.List;

public interface ClinicScheduleService {

    ClinicSchedule getById(Long id);

    List<ClinicSchedule> getAll();

    ClinicSchedule save(CreateClinicSchedulesDto clinicSchedule,String authHeader) throws Exception;

    ClinicSchedule update(ClinicSchedule clinicSchedule,String authHeader) throws Exception;

    public List<ClinicSchedule> generateSchedules(Long settingsId,String authHeader) throws Exception;

    void delete(Long id,String authHeader) throws Exception;

    void deleteGrouped(Long id, String date,String authHeader) throws Exception;

    ClinicScheduleResultDto getAllByClinicGroupedByDate( PageRequest pageable, String sort,String authHeader) throws Exception;

    List<ClinicSchedule> getAllByDoctorId(Long doctorId,String authHeader) throws Exception;
}

