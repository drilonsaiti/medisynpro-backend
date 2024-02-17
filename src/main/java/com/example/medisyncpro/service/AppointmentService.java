package com.example.medisyncpro.service;

import com.example.medisyncpro.model.Appointment;
import com.example.medisyncpro.model.dto.*;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface AppointmentService {

    Appointment getById(Long id,String authHeader);

    AppointmentResultDto getAll(PageRequest pageable, String nameOrEmail, String types,String authHeader);

    AppointmentResultDto getMyAppointment(PageRequest pageable,String authHeader);


    List<AppointmentDto> getAllByPatient(Long id,String authHeader);

    List<AppointmentDto> getAllByDoctor(Long id,String authHeader);

    Appointment save(CreateAppointmentDto appointment,String authHeader);

    Appointment update(AppointmentDto appointment,String authHeader) throws Exception;

    void delete(Long id,String authHeader) throws Exception;

    List<AppointmentDateDto> getAppointmentDates(String authHeader) throws Exception;

    Appointment createAppointmentByReceptionist(AppointmentByReceptionistDto dto,String authHeader) throws Exception;

    void changeAttended(Long id, boolean attended,String authHeader);

    AppointmentDto nexAppointment(Long id,String authHeader);
}

