package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.ClinicSchedule;
import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.dto.ClinicScheduleDto;
import com.example.medisyncpro.model.dto.CreateClinicSchedulesDto;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ClinicScheduleMapper {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ClinicSchedule createClinicSchedule(CreateClinicSchedulesDto dto) {
        return new ClinicSchedule(
                dto.getDoctorId(),
                dto.getClinicId(),
                dto.getDate(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }

    public ClinicSchedule updateClinicSchedule(ClinicSchedule old, ClinicSchedule newSchedule) {
        old.setDoctorId(newSchedule.getDoctorId());
        old.setClinicId(newSchedule.getClinicId());
        old.setDate(newSchedule.getDate());
        old.setStartTime(newSchedule.getStartTime());
        old.setEndTime(newSchedule.getEndTime());
        old.setIsBooked(newSchedule.getIsBooked());

        return old;
    }

    public static ClinicSchedule mapToClinicSchedule(Doctor doctor, LocalDate currentDate, LocalDateTime currentDateTime, int appointmentDurationMinutes) {
        ClinicSchedule schedule = new ClinicSchedule();
        schedule.setDoctorId(doctor.getDoctorId());
        schedule.setClinicId(doctor.getClinic().getClinicId());
        schedule.setDate(Date.valueOf(currentDate));
        schedule.setStartTime(currentDateTime);
        schedule.setEndTime(currentDateTime.plusMinutes(appointmentDurationMinutes));
        schedule.setIsBooked(false);
        return schedule;
    }

    private static String formatTimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        String formattedStartTime = startTime.format(TIME_FORMATTER);
        String formattedEndTime = endTime.format(TIME_FORMATTER);
        return formattedStartTime + "-" + formattedEndTime;
    }

    public ClinicScheduleDto clinicScheduleToDto(ClinicSchedule schedule, String doctorName) {
        return new ClinicScheduleDto(
                schedule.getScheduleId(),
                schedule.getDate(),
                doctorName,
                formatTimeSlot(schedule.getStartTime(), schedule.getEndTime()),
                schedule.getIsBooked()
        );
    }
}
