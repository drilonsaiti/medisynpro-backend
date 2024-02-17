package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.Settings;
import com.example.medisyncpro.model.dto.DoctorSettingsDto;
import com.example.medisyncpro.model.dto.SettingsDTO;
import com.example.medisyncpro.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingsMapper {

    private final DoctorService doctorService;

    public SettingsDTO toDTO(Settings settings) {
        SettingsDTO dto = new SettingsDTO();
        dto.setId(settings.getId());
        dto.setClinicId(settings.getClinicId());
        dto.setMorningStartTime(settings.getMorningStartTime());
        dto.setMorningEndTime(settings.getMorningEndTime());
        dto.setAfternoonStartTime(settings.getAfternoonStartTime());
        dto.setAfternoonEndTime(settings.getAfternoonEndTime());
        dto.setAppointmentDurationMinutes(settings.getAppointmentDurationMinutes());
        dto.setDaysToGenerate(settings.getDaysToGenerate());
        // Map morning doctors
        List<DoctorSettingsDto> morningDoctorDTOs = settings.getMorningDoctors() != null ? settings.getMorningDoctors().stream()
                .map(doctor -> {
                    DoctorSettingsDto doctorDto = new DoctorSettingsDto();
                    doctorDto.setDoctorId(doctor.getDoctorId());
                    doctorDto.setDoctorName(doctor.getDoctorName());
                    return doctorDto;
                }).toList() : new ArrayList<>();
        dto.setMorningDoctors(morningDoctorDTOs);
        // Map afternoon doctors
        List<DoctorSettingsDto> afternoonDoctorDTOs =settings.getAfternoonDoctors() != null ? settings.getAfternoonDoctors().stream()
                .map(doctor -> {
                    DoctorSettingsDto doctorDto = new DoctorSettingsDto();
                    doctorDto.setDoctorId(doctor.getDoctorId());
                    doctorDto.setDoctorName(doctor.getDoctorName());
                    return doctorDto;
                }).toList() : new ArrayList<>();
        dto.setAfternoonDoctors(afternoonDoctorDTOs);
        // Map other fields as needed
        return dto;
    }

    public Settings updateSettings(SettingsDTO dto, Settings settings) {
        settings.setMorningStartTime(dto.getMorningStartTime());
        settings.setMorningEndTime(dto.getMorningEndTime());
        settings.setAfternoonStartTime(dto.getAfternoonStartTime());
        settings.setAfternoonEndTime(dto.getAfternoonEndTime());
        settings.setAppointmentDurationMinutes(dto.getAppointmentDurationMinutes());
        settings.setDaysToGenerate(dto.getDaysToGenerate());
        List<Doctor> morningDoctors = new ArrayList<>();
        if (!settings.getMorningDoctors().isEmpty() || (dto.getMorningDoctors() != null)) {
            settings.getMorningDoctors().clear();
            morningDoctors = dto.getMorningDoctors().stream()
                    .filter(doc -> doc.getDoctorId() != null)
                    .map(doc -> doctorService.getById(doc.getDoctorId()))
                    .toList();
        }

        settings.getMorningDoctors().addAll(morningDoctors);

        List<Doctor> afternoonDoctors = new ArrayList<>();
        if (!settings.getAfternoonDoctors().isEmpty() || (dto.getAfternoonDoctors() != null)) {
            settings.getAfternoonDoctors().clear();
            afternoonDoctors = dto.getAfternoonDoctors().stream()
                    .filter(doc -> doc.getDoctorId() != null)
                    .map(doc -> doctorService.getById(doc.getDoctorId()))
                    .toList();
        }


        settings.getAfternoonDoctors().addAll(afternoonDoctors);

        return settings;
    }
}
