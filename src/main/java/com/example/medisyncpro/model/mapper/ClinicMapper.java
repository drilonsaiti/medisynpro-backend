package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.Clinic;
import com.example.medisyncpro.model.ClinicServices;
import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.Settings;
import com.example.medisyncpro.model.dto.ClinicDto;
import com.example.medisyncpro.model.dto.ServiceBySpecializationIdDto;
import com.example.medisyncpro.model.dto.UpdateClinicDto;
import com.example.medisyncpro.service.ClinicServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicMapper {

    private final ClinicServicesService service;

    private List<ServiceBySpecializationIdDto> services(List<ClinicServices> services) {

        return services.stream().map(s -> new ServiceBySpecializationIdDto(
                s.getSpecializations().getSpecializationId(),
                s.getServiceName()
        )).toList();
    }

    public ClinicDto getClinicDto(Clinic clinic, Settings settings) {
        return new ClinicDto(
                clinic.getClinicId(),
                clinic.getClinicName(),
                clinic.getEmailAddress(),
                clinic.getAddress(),
                clinic.getImageUrl(),
                clinic.getSpecializations(),
                services(clinic.getServices()),
                clinic.getDoctors(),
                settings != null && settings.getMorningStartTime() != null ? settings.getMorningStartTime().toString() + "-" + settings.getMorningEndTime() : null,
                settings != null && settings.getAfternoonStartTime() != null ? settings.getAfternoonStartTime().toString() + "-" + settings.getAfternoonEndTime() : null
        );
    }

    public Clinic updateClinic(Clinic old, UpdateClinicDto newClinic,
                               List<ClinicServices> clinicServices, List<Doctor> doctors) {
        old.setClinicName(newClinic.getClinicName());
        old.setAddress(newClinic.getAddress());
        old.getSpecializations().clear();
        old.getSpecializations().addAll(newClinic.getSpecializations());
        old.getServices().clear();
        old.getServices().addAll(clinicServices);
        old.getDoctors().clear();
        old.getDoctors().addAll(doctors);
        return old;

    }
}
