package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.config.JwtService;
import com.example.medisyncpro.model.Appointment;
import com.example.medisyncpro.model.enums.Role;
import com.example.medisyncpro.repository.*;
import com.example.medisyncpro.service.AuthHeaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthHeaderServiceImpl implements AuthHeaderService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository;
    private final ReceptionistRepository receptionistRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private Long getIdByRole(String email) throws Exception {
        Role role = userRepository.findByEmail(email).get().getRole();
        switch (role){
            case ROLE_OWNER:
                return clinicRepository.findByEmailAddress(email).getClinicId();
            case ROLE_DOCTOR:
                return doctorRepository.findByEmail(email).get().getClinic().getClinicId();
            case ROLE_RECEPTIONIST:
                return receptionistRepository.findByEmailAddress(email).get().getClinicId();
            default:
                return null;
        }
    }

    private boolean getPatientIdByRole(String email, Appointment a) throws Exception {
        Long clinicId = getIdByRole(email);

        if(clinicId != null){
            return Objects.equals(a.getClinicId(), clinicId);
        }
        Long patientId = patientRepository.findByEmail(email).getPatientId();

        return Objects.equals(a.getPatientId(), patientId);
    }

    @Override
    public String getEmail(String authHeader){
        final String jwt;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);

        return email;
    }

    @Override
    public Long getClinicId(String authHeader) throws Exception {
        final String jwt;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);

        return getIdByRole(email);

    }

    @Override
    public boolean isPatientClinic(String authHeader,Appointment a) throws Exception {
        final String jwt;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);

        return getPatientIdByRole(email,a);

    }
}
