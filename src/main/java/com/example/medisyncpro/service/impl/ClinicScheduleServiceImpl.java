package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.ClinicSchedule;
import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.Settings;
import com.example.medisyncpro.model.dto.ClinicScheduleDto;
import com.example.medisyncpro.model.dto.ClinicScheduleResultDto;
import com.example.medisyncpro.model.dto.CreateClinicSchedulesDto;
import com.example.medisyncpro.model.dto.GroupedClinicSchedule;
import com.example.medisyncpro.model.excp.ClinicScheduleException;
import com.example.medisyncpro.model.mapper.ClinicScheduleMapper;
import com.example.medisyncpro.repository.ClinicScheduleRepository;
import com.example.medisyncpro.repository.DoctorRepository;
import com.example.medisyncpro.repository.SettingsRepository;
import com.example.medisyncpro.service.ClinicScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClinicScheduleServiceImpl implements ClinicScheduleService {


    private final ClinicScheduleRepository clinicScheduleRepository;
    private final SettingsRepository settingsRepository;
    private final ClinicScheduleMapper scheduleMapper;
    private final DoctorRepository doctorRepository;
    private final AuthHeaderServiceImpl authHeaderService;

    @Override
    public ClinicSchedule getById(Long id) {
        try {
            return clinicScheduleRepository.findById(id).orElseThrow(() -> new ClinicScheduleException("Clinic schedule with ID " + id + " not found"));
        } catch (Exception e) {
            throw new ClinicScheduleException("Failed to retrieve clinic schedule by ID", e);
        }
    }

    @Override
    public List<ClinicSchedule> getAll() {
        try {
            return clinicScheduleRepository.findAll();
        } catch (Exception e) {
            throw new ClinicScheduleException("Failed to retrieve all clinic schedules");
        }
    }

    private Comparable getFieldValue(
            GroupedClinicSchedule booking, String field) {
        if (field.equals("startDate")) {
            return booking.getDate();
        }
        throw new IllegalArgumentException("Invalid field for sorting: " + field);
    }

    @Override
    public ClinicSchedule save(CreateClinicSchedulesDto clinicSchedule,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(clinicId, clinicSchedule.getClinicId())) {
            try {
                return clinicScheduleRepository.save(scheduleMapper.createClinicSchedule(clinicSchedule));
            } catch (Exception e) {
                throw new ClinicScheduleException("Error saving clinic schedule", e);
            }
        }
        throw new Exception("You don't have access!");
    }

    @Override
    public ClinicSchedule update(ClinicSchedule clinicSchedule,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(clinicId, clinicSchedule.getClinicId())) {
            try {
                ClinicSchedule old = this.getById(clinicSchedule.getScheduleId());
                return clinicScheduleRepository.save(scheduleMapper.updateClinicSchedule(old, clinicSchedule));
            } catch (Exception e) {
                throw new ClinicScheduleException("Error updating clinic schedule", e);
            }
        }
        throw new Exception("You don't have access!");
    }

    @Override
    public void delete(Long id,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        ClinicSchedule clinicSchedule = this.clinicScheduleRepository.findByScheduleId(id);

        if (Objects.equals(clinicId, clinicSchedule.getClinicId())) {
            try {
                clinicScheduleRepository.deleteById(id);
            } catch (Exception e) {
                throw new ClinicScheduleException("Error deleting clinic schedule", e);
            }
        }
        throw new Exception("You don't have access!");
    }

    @Override
    public void deleteGrouped(Long id, String date,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        if (Objects.equals(clinicId, id)) {
            try {
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(date);
                Date newDate = Date.from(offsetDateTime.toInstant());

                List<ClinicSchedule> clinicSchedules = clinicScheduleRepository.findAllByClinicIdAndDate(id, newDate);

                for (ClinicSchedule c : clinicSchedules) {
                    this.delete(c.getScheduleId(),authHeader);
                }
            } catch (Exception e) {
                throw new ClinicScheduleException("Error deleting grouped clinic schedules", e);
            }
        }
    }

    public List<ClinicSchedule> generateSchedules(Long settingsId,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        try {
            List<ClinicSchedule> schedules = new ArrayList<>();
            Settings settings = settingsRepository.getById(settingsId);
            LocalDate currentDate = LocalDate.now();

            if(clinicId == settings.getClinicId()) {
                for (int i = 0; i < settings.getDaysToGenerate(); i++) {
                    LocalDateTime currentDateTime = LocalDateTime.of(currentDate, settings.getMorningStartTime());

                    while (currentDateTime.isBefore(LocalDateTime.of(currentDate, settings.getMorningEndTime()))) {
                        for (Doctor doctor : settings.getMorningDoctors()) {
                            ClinicSchedule schedule = ClinicScheduleMapper.mapToClinicSchedule(doctor, currentDate, currentDateTime, settings.getAppointmentDurationMinutes());

                            schedules.add(schedule);
                        }
                        currentDateTime = currentDateTime.plusMinutes(settings.getAppointmentDurationMinutes());
                    }

                    currentDateTime = LocalDateTime.of(currentDate, settings.getAfternoonStartTime());

                    while (currentDateTime.isBefore(LocalDateTime.of(currentDate, settings.getAfternoonEndTime()))) {
                        for (Doctor doctor : settings.getAfternoonDoctors()) {
                            ClinicSchedule schedule = ClinicScheduleMapper.mapToClinicSchedule(doctor, currentDate, currentDateTime, settings.getAppointmentDurationMinutes());
                            schedules.add(schedule);
                        }
                        currentDateTime = currentDateTime.plusMinutes(settings.getAppointmentDurationMinutes());
                    }

                    currentDate = currentDate.plusDays(1);
                }
                this.clinicScheduleRepository.saveAll(schedules);
                return schedules;
            }
        } catch (Exception e) {
            throw new ClinicScheduleException("Error generating schedules", e);
        }
        throw new ClinicScheduleException("You don't have access!");
    }


    @Override
    public ClinicScheduleResultDto getAllByClinicGroupedByDate(PageRequest pageable, String sort,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);


        try {
            List<ClinicSchedule> schedules = clinicScheduleRepository.findAllByClinicId(clinicId);

            List<ClinicScheduleDto> schedulesDto = schedules.stream()
                    .map(schedule -> {
                        Doctor doctor = doctorRepository.getById(schedule.getDoctorId());
                        return scheduleMapper.clinicScheduleToDto(schedule, doctor.getDoctorName());
                    }).toList();

            Map<Date, List<ClinicScheduleDto>> groupedSchedules = schedulesDto.stream()
                    .collect(Collectors.groupingBy(ClinicScheduleDto::getDate));

            List<GroupedClinicSchedule> groupedClinicSchedules = groupedSchedules.entrySet().stream()
                    .map(entry -> new GroupedClinicSchedule(clinicId, entry.getKey(), entry.getValue()))
                    .sorted((a, b) -> {
                        String[] sortBy = sort.split("-");
                        String field = sortBy[0];
                        String direction = sortBy[1];
                        Comparable fieldA = getFieldValue(a, field);
                        Comparable fieldB = getFieldValue(b, field);

                        return direction.equals("asc") ? fieldA.compareTo(fieldB) : fieldB.compareTo(fieldA);
                    })
                    .skip(pageable.getOffset()).limit(pageable.getPageSize()).toList();

            return new ClinicScheduleResultDto(groupedClinicSchedules, groupedClinicSchedules.size());
        } catch (Exception e) {
            throw new ClinicScheduleException("Error getting clinic schedules grouped by date", e);
        }
    }


    @Override
    public List<ClinicSchedule> getAllByDoctorId(Long doctorId,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        return this.clinicScheduleRepository.findAllByDoctorId(doctorId).stream().filter(c -> !c.getIsBooked() && Objects.equals(c.getClinicId(), clinicId))
                .filter(c -> c.getDate().after(new Date())).toList();
    }
}
