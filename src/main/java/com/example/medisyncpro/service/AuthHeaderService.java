package com.example.medisyncpro.service;

import com.example.medisyncpro.model.Appointment;

public interface AuthHeaderService {

    Long getClinicId(String authHeader) throws Exception;

    boolean isPatientClinic(String authHeader, Appointment a) throws Exception;

    String getEmail(String authHeader);
}
