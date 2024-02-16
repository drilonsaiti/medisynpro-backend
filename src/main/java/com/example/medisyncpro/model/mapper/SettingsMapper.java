package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.Settings;
import com.example.medisyncpro.model.dto.DoctorSettingsDto;
import com.example.medisyncpro.model.dto.SettingsDTO;
import com.example.medisyncpro.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        List<DoctorSettingsDto> morningDoctorDTOs = settings.getMorningDoctors().stream()
                .map(doctor -> {
                    DoctorSettingsDto doctorDto = new DoctorSettingsDto();
                    doctorDto.setDoctorId(doctor.getDoctorId());
                    doctorDto.setDoctorName(doctor.getDoctorName());
                    return doctorDto;
                }).toList();
        dto.setMorningDoctors(morningDoctorDTOs);
        // Map afternoon doctors
        List<DoctorSettingsDto> afternoonDoctorDTOs = settings.getAfternoonDoctors().stream()
                .map(doctor -> {
                    DoctorSettingsDto doctorDto = new DoctorSettingsDto();
                    doctorDto.setDoctorId(doctor.getDoctorId());
                    doctorDto.setDoctorName(doctor.getDoctorName());
                    return doctorDto;
                }).toList();
        dto.setAfternoonDoctors(afternoonDoctorDTOs);
        // Map other fields as needed
        return dto;
    }

    public Settings updateSettings(SettingsDTO dto, Settings settings) {
        settings.setId(dto.getId());
        settings.setClinicId(dto.getClinicId());
        settings.setMorningStartTime(dto.getMorningStartTime());
        settings.setMorningEndTime(dto.getMorningEndTime());
        settings.setAfternoonStartTime(dto.getAfternoonStartTime());
        settings.setAfternoonEndTime(dto.getAfternoonEndTime());
        settings.setAppointmentDurationMinutes(dto.getAppointmentDurationMinutes());
        settings.setDaysToGenerate(dto.getDaysToGenerate());

        settings.getMorningDoctors().clear();
        List<Doctor> morningDoctors = dto.getMorningDoctors().stream()
                .filter(doc -> doc.getDoctorId() != null)
                .map(doc -> doctorService.getById(doc.getDoctorId()))
                .toList();
        settings.getMorningDoctors().addAll(morningDoctors);

        settings.getAfternoonDoctors().clear();
        List<Doctor> afternoonDoctors = dto.getAfternoonDoctors().stream()
                .filter(doc -> doc.getDoctorId() != null)
                .map(doc -> doctorService.getById(doc.getDoctorId()))
                .toList();
        settings.getAfternoonDoctors().addAll(afternoonDoctors);

        return settings;
    }
}
