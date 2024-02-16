package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.*;
import com.example.medisyncpro.model.dto.CreateMedicalReportDto;
import com.example.medisyncpro.model.dto.MedicalReportDto;
import com.example.medisyncpro.model.dto.MedicalReportResultDto;
import com.example.medisyncpro.model.dto.ServiceDto;
import com.example.medisyncpro.model.excp.MedicalReportException;
import com.example.medisyncpro.model.mapper.MedicalReportMapper;
import com.example.medisyncpro.repository.*;
import com.example.medisyncpro.service.AuthHeaderService;
import com.example.medisyncpro.service.MedicalReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalReportServiceImpl implements MedicalReportService {

    private final MedicalReportRepository medicalReportRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalReportMapper reportMapper;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final AuthHeaderService authHeaderService;

    @Override
    public MedicalReportDto getById(Long id,String authHeader) throws Exception {
        if (id == null) {
            return null;
        }
        Patient p = this.patientRepository.findByEmail(authHeaderService.getEmail(authHeader));
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        MedicalReport medicalReport = medicalReportRepository.findByReportId(id).orElse(null);

        if (medicalReport != null && (Objects.equals(clinicId, medicalReport.getClinicId()) ||
                Objects.equals(p.getPatientId(), medicalReport.getPatientId()))) {
            return medicalReportRepository.findById(id)
                    .map(report -> {
                        Patient patient = patientRepository.getById(report.getPatientId());
                        Appointment appointment = appointmentRepository.findByDateAndPatientId(report.getAppointmentDate(), patient.getPatientId());
                        AtomicInteger totalPrice = new AtomicInteger();
                        List<ServiceDto> services = appointment.getServiceIds().stream()
                                .map(service -> {
                                    ClinicServices s = serviceRepository.getById(service);
                                    totalPrice.addAndGet(s.getPrice().intValue());
                                    return new ServiceDto(s.getServiceName(), s.getDurationMinutes(), s.getPrice());
                                }).collect(Collectors.toList());
                        return reportMapper.getMedicalReport(report, patient.getPatientId(), patient.getPatientName(), patient.getEmail(), services, totalPrice.get());

                    })
                    .orElse(null);
        }
        throw new MedicalReportException("You don't have access!");
    }

    @Override
    public MedicalReportResultDto getAll(PageRequest page, String nameOrEmail, String byDate,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");
            LocalDateTime dateTime = !byDate.equals("all") ? LocalDateTime.parse(byDate, formatter) : LocalDateTime.now();

            List<MedicalReportDto> reports = medicalReportRepository.findAll().stream()
                    .filter(report -> Objects.equals(report.getClinicId(), clinicId))
                    .filter(report -> {
                        try {
                            Patient p = patientRepository.getById(report.getPatientId());
                            return (((nameOrEmail.equals("all") || p.getPatientName().toLowerCase().contains(nameOrEmail.toLowerCase()) || p.getEmail().toLowerCase().contains(nameOrEmail.toLowerCase())))
                                    && (byDate.equals("all") || (
                                    !dateTime.toLocalTime().isAfter(LocalTime.of(0, 0)) ?
                                            report.getAppointmentDate().toLocalDate().isEqual(dateTime.toLocalDate()) :
                                            report.getAppointmentDate().isEqual(dateTime)
                            )));
                        } catch (Exception e) {
                            throw new MedicalReportException("Error filtering medical reports", e);
                        }
                    })
                    .map(report -> {
                        try {
                            Patient patient = patientRepository.getById(report.getPatientId());
                            Appointment appointment = appointmentRepository.findByDateAndPatientId(report.getAppointmentDate(), patient.getPatientId());
                            AtomicInteger totalPrice = new AtomicInteger();
                            List<ServiceDto> services = appointment.getServiceIds().stream()
                                    .map(service -> {
                                        ClinicServices s = serviceRepository.getById(service);
                                        totalPrice.addAndGet(s.getPrice().intValue());
                                        return new ServiceDto(s.getServiceName(), s.getDurationMinutes(), s.getPrice());
                                    }).collect(Collectors.toList());
                            return reportMapper.getMedicalReport(report, patient.getPatientId(), patient.getPatientName(), patient.getEmail(), services, totalPrice.get());
                        } catch (Exception e) {
                            throw new MedicalReportException("Error mapping medical reports", e);
                        }
                    }).collect(Collectors.toList());

            return new MedicalReportResultDto(
                    reports.stream()
                            .skip(page.getOffset())
                            .limit(page.getPageSize()).collect(Collectors.toList()), reports.size()
            );
        } catch (Exception e) {
            throw new MedicalReportException("Error retrieving medical reports", e);
        }
    }


    @Override
    public MedicalReportResultDto getMedicalReportByPatient(PageRequest page, String authHeader) {
        String email = authHeaderService.getEmail(authHeader);
        Patient patient = this.patientRepository.findByEmail(email);

        List<MedicalReportDto> reports = this.medicalReportRepository.findAllByPatientId(patient.getPatientId())
                .stream()
                .map(report -> {
                    try {
                        Appointment appointment = appointmentRepository.findByDateAndPatientId(report.getAppointmentDate(), patient.getPatientId());
                        AtomicInteger totalPrice = new AtomicInteger();
                        List<ServiceDto> services = appointment.getServiceIds().stream()
                                .map(service -> {
                                    ClinicServices s = serviceRepository.getById(service);
                                    totalPrice.addAndGet(s.getPrice().intValue());
                                    return new ServiceDto(s.getServiceName(), s.getDurationMinutes(), s.getPrice());
                                }).collect(Collectors.toList());
                        return reportMapper.getMedicalReport(report, patient.getPatientId(), patient.getPatientName(), patient.getEmail(), services, totalPrice.get());
                    } catch (Exception e) {
                        throw new MedicalReportException("Error mapping medical reports", e);
                    }
                }).toList();

        return new MedicalReportResultDto(
                reports.stream()
                        .skip(page.getOffset())
                        .limit(page.getPageSize()).collect(Collectors.toList()), reports.size()
        );
    }

    @Override
    public MedicalReport save(CreateMedicalReportDto medicalReport,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        Appointment appointment = appointmentRepository.findByAppointmentId(medicalReport.getAppointmentId());
        if (Objects.equals(clinicId, appointment.getClinicId())) {

            try {

                Doctor doctor = doctorRepository.findByDoctorId(appointment.getDoctorId()).orElse(null);

                return medicalReportRepository.save(reportMapper.createMedicalReport(medicalReport, doctor, appointment.getDate(), appointment.getPatientId(), appointment.getClinicId()));
            } catch (Exception e) {
                throw new MedicalReportException("Error saving medical report", e);
            }
        }
        throw new MedicalReportException("You don't have access");
    }

    @Override
    public MedicalReport update(MedicalReport medicalReport,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);

        return medicalReportRepository.findById(medicalReport.getReportId())
                .filter(report -> Objects.equals(report.getClinicId(), clinicId))
                .map(old -> {
                    try {
                        return medicalReportRepository.save(reportMapper.updateMedicalReport(old, medicalReport));
                    } catch (Exception e) {
                        throw new MedicalReportException("Error updating medical report", e);
                    }
                })
                .orElseThrow(() -> new MedicalReportException("Medical report with id " + medicalReport.getReportId() + " not found"));
    }

    @Override
    public void delete(Long id,String authHeader) throws Exception {
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        MedicalReport report = this.medicalReportRepository.findByReportId(id).orElse(null);

        if (report != null && Objects.equals(report.getClinicId(), clinicId)) {
            try {
                medicalReportRepository.deleteById(id);
            } catch (Exception e) {
                throw new MedicalReportException("Error deleting medical report", e);
            }
        }

    }
}
