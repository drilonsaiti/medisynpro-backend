package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.Clinic;
import com.example.medisyncpro.model.ClinicServices;
import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.Settings;
import com.example.medisyncpro.model.dto.*;
import com.example.medisyncpro.model.excp.ClinicException;
import com.example.medisyncpro.model.mapper.ClinicMapper;
import com.example.medisyncpro.repository.*;
import com.example.medisyncpro.service.ClinicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {


    private final ClinicRepository clinicRepository;
    private final SettingsRepository settingsRepository;
    private final ClinicMapper clinicMapper;
    private final ServiceRepository serviceRepository;
    private final ClinicScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final AuthHeaderServiceImpl authHeaderService;

    @Override
    public Clinic getById(Long id) {
        try {
            return clinicRepository.findById(id).orElseThrow(() -> new ClinicException("Clinic with ID " + id + " not found"));
        } catch (Exception e) {
            throw new ClinicException("Failed to retrieve clinic by ID", e);
        }

    }

    @Override
    public ClinicDto getByIdDto(Long id) {

        try {
            Clinic clinic = this.getById(id);
            Settings settings = settingsRepository.getById(clinic.getSettingsId());
            return clinicMapper.getClinicDto(clinic, settings);
        } catch (Exception e) {
            throw new ClinicException("Failed to retrieve clinic  by ID", e);
        }
    }

    @Override
    public ClinicDto getByIdAuth(String authHeader) throws Exception {
        Long clinicId = authHeaderService.getClinicId(authHeader);
        Clinic clinic = this.getById(clinicId);
        return clinicMapper.getClinicDto(clinic,null);
    }

    @Override
    public ClinicDto getMyProfile(String authHeader) {
        String email = this.authHeaderService.getEmail(authHeader);
        try {
            Clinic clinic = this.clinicRepository.findByEmailAddress(email);
            Settings settings = null;
            if (clinic.getSettingsId() != null) {
                settings = settingsRepository.getById(clinic.getSettingsId());
            }
            return clinicMapper.getClinicDto(clinic, settings);
        } catch (Exception e) {
            throw new ClinicException("Failed to retrieve clinic by email:"+email, e);
        }
    }

    @Override
    public ClinicResultDto getAll(PageRequest pageable, String specializations, String service, String byDate) {
        try {
            String[] specs = specializations.split(",");
            String[] services = service.split(",");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");

            LocalDateTime dateTime = !byDate.equals("all") ? LocalDateTime.parse(byDate, formatter) : LocalDateTime.now();

            List<ClinicDto> clinics = clinicRepository.findAll()
                    .stream()
                    .filter(clinic ->
                            (specializations.equals("all") || clinic.getSpecializations()
                                    .stream().anyMatch(specialization ->
                                            Arrays.asList(specs).contains(specialization.getSpecializationName())))
                                    &&
                                    (service.equals("all") || clinic.getServices()
                                            .stream()
                                            .anyMatch(srv -> Arrays.asList(services).contains(srv.getServiceName())))
                                    && (byDate.equals("all") || scheduleRepository.findAllByClinicId(clinic.getClinicId()).stream().anyMatch(
                                    schedule ->
                                            !dateTime.toLocalTime().isAfter(LocalTime.of(0, 0)) ?
                                                    (schedule.getStartTime().toLocalDate().isEqual(dateTime.toLocalDate()) && !schedule.getIsBooked()) :
                                                    (schedule.getStartTime().isEqual(dateTime) && !schedule.getIsBooked())
                            ))
                    )
                    .map(clinic -> clinicMapper
                            .getClinicDto(clinic, settingsRepository.findById(clinic.getClinicId()).orElse(null))).toList();

            int totalElements = clinics.size();

            return new ClinicResultDto(clinics
                    .stream()
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize()).toList(), totalElements
            );
        } catch (Exception e) {
            throw new ClinicException("Failed to retrieve all clinics", e);
        }
    }

    @Override
    public Clinic save(Clinic clinic) {
        try {
            return clinicRepository.save(clinic);
        } catch (Exception e) {
            throw new ClinicException("Failed to save clinic", e);
        }
    }


    @Override
    public Clinic updateClinic(UpdateClinicDto dto, String auth) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(auth);

        if (Objects.equals(clinicId, dto.getClinicId())) {

            try {
                List<Long> serviceIds = dto.getServiceDto().stream()
                        .map(ServiceForClinicsDto::getServiceId)
                        .toList();

                List<ClinicServices> clinicServices = serviceRepository.findAll().stream()
                        .filter(srv -> serviceIds.contains(srv.getServiceId()))
                        .toList();

                List<Long> doctorIds = dto.getDoctors().stream()
                        .map(DoctorForClinicDto::getDoctorId)
                        .toList();

                List<Doctor> doctors = doctorRepository.findAll().stream()
                        .filter(srv -> doctorIds.contains(srv.getDoctorId()))
                        .toList();

                Clinic clinic = this.getById(dto.getClinicId());
                return clinicRepository.save(clinicMapper.updateClinic(clinic, dto, clinicServices, doctors));
            } catch (Exception e) {
                throw new ClinicException("Failed to update clinic", e);
            }
        }
        throw new ClinicException("You don't have access");

    }

    @Override
    public void delete(Long id, String auth) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(auth);

        if (Objects.equals(clinicId, id)) {
            try {
                clinicRepository.deleteById(id);
            } catch (Exception e) {
                throw new ClinicException("Failed to delete clinic", e);
            }
        }
        throw new ClinicException("You don't have access");

    }

    @Override
    public long getTotalClinicCount(String specialization, String status) {
        return 0;
    }

    @Override
    public List<ClinicServices> getClinicServicesById(Long id, String auth) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(auth);

        if (Objects.equals(clinicId, id)) {
            List<ClinicServices> services = this.getById(id).getServices();
            return services;
        }
        throw new ClinicException("You don't have access");

    }
}

