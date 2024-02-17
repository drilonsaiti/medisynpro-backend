package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.*;
import com.example.medisyncpro.model.dto.*;
import com.example.medisyncpro.model.excp.ClinicAppointmentException;
import com.example.medisyncpro.model.excp.PatientException;
import com.example.medisyncpro.model.mapper.AppointmentMapper;
import com.example.medisyncpro.repository.*;
import com.example.medisyncpro.service.AppointmentService;
import com.example.medisyncpro.service.MedicalReportService;
import com.example.medisyncpro.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final ServiceRepository serviceRepository;
    private final ClinicScheduleRepository clinicScheduleRepository;
    private final PatientService patientService;
    private final DoctorRepository doctorRepository;
    private final MedicalReportRepository reportRepository;
    private final MedicalReportService medicalReportService;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final AuthHeaderServiceImpl authHeaderService;
    private final ReceptionistRepository receptionistRepository;

    @Override
    public Appointment getById(Long id,String authHeader) {
        return appointmentRepository.findById(id).filter(a -> {
                    try {
                        return authHeaderService.isPatientClinic(authHeader,a);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new ClinicAppointmentException("Appointment with ID " + id + " not found"));
    }

    @Override
    public AppointmentResultDto getAll(PageRequest pageable, String nameOrEmail, String types,String authHeader) throws ClinicAppointmentException {

        List<AppointmentDto> appointment = appointmentRepository.findAll().stream()
                .filter(a -> {
                    try {
                        return Objects.equals(authHeaderService.getClinicId(authHeader), a.getClinicId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(a -> a.getDate().toLocalDate().isEqual(LocalDate.now()) || a.getDate().toLocalDate().isAfter(LocalDate.now()))
                .filter(a -> {
                    Patient p = this.patientService.getById(a.getPatientId());
                    return ((nameOrEmail.equals("all") || p.getPatientName().toLowerCase().contains(nameOrEmail.toLowerCase())
                            || p.getEmail().toLowerCase().contains(nameOrEmail.toLowerCase())));
                })
                .filter(a -> types.equals("all") ||
                        a.getDate().toLocalDate().isEqual(LocalDate.now()))
                .map(appm -> {
                    Optional<Patient> patient = Optional.ofNullable(patientRepository.findById(appm.getPatientId()).orElse(null));
                    Optional<Clinic> clinic = Optional.ofNullable(clinicRepository.findByClinicId(appm.getClinicId()).orElse(null));
                    Optional<Doctor> doctor = Optional.ofNullable(this.doctorRepository.findByDoctorId(appm.getDoctorId()).orElse(null));
                    MedicalReport report = this.reportRepository.findByAppointmentId(appm.getAppointmentId()).orElse(null);
                    MedicalReportDto reportDto = null;
                    try {
                        reportDto = medicalReportService.getById(report != null ? report.getReportId() : null,authHeader);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    List<String> services = appm.getServiceIds().stream().map(service -> {
                        Optional<ClinicServices> s = this.serviceRepository.findByServiceId(service);
                        return s.isPresent() ? s.get().getServiceName() : null;
                    }).filter(Objects::nonNull).toList();
                    return appointmentMapper.getAppointment(appm, patient.get(), doctor.get(), services, reportDto, clinic.get().getClinicName());
                }).toList();

        return new AppointmentResultDto(appointment.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize()).toList(), appointment.size());

    }

    @Override
    public AppointmentResultDto getMyAppointment(PageRequest pageable, String authHeader) {
        String email = authHeaderService.getEmail(authHeader);
        Patient patient = this.patientRepository.findByEmail(email);

        List<AppointmentDto> appointment = this.appointmentRepository.findAllByPatientId(patient.getPatientId())
                .stream()
                .map(appm -> {
                    Optional<Clinic> clinic = Optional.ofNullable(clinicRepository.findByClinicId(appm.getClinicId()).orElse(null));
                    Optional<Doctor> doctor = Optional.ofNullable(this.doctorRepository.findByDoctorId(appm.getDoctorId()).orElse(null));
                    MedicalReport report = this.reportRepository.findByAppointmentId(appm.getAppointmentId()).orElse(null);
                    MedicalReportDto reportDto = null;
                    try {
                        reportDto = medicalReportService.getById(report != null ? report.getReportId() : null,authHeader);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    List<String> services = appm.getServiceIds().stream().map(service -> {
                        Optional<ClinicServices> s = this.serviceRepository.findByServiceId(service);
                        return s.isPresent() ? s.get().getServiceName() : null;
                    }).filter(Objects::nonNull).toList();
                    return appointmentMapper.getAppointment(appm, patient, doctor.get(), services, reportDto, clinic.get().getClinicName());
                }).toList();

        return new AppointmentResultDto(appointment.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize()).toList(), appointment.size());
    }

    @Override
    public List<AppointmentDto> getAllByPatient(Long id,String authHeader) throws ClinicAppointmentException {
        Patient p = this.patientRepository.findByEmail(this.authHeaderService.getEmail(authHeader));
        if(p.getPatientId() == id) {
            try {
                return appointmentRepository.findAllByPatientId(id).stream().map(appm -> {
                    try {
                        Patient patient = patientService.getById(appm.getPatientId());
                        Doctor doctor = this.doctorRepository.getById(appm.getDoctorId());
                        Optional<Clinic> clinic = clinicRepository.findByClinicId(appm.getClinicId());

                        MedicalReport report = this.reportRepository.findByAppointmentId(appm.getAppointmentId()).orElse(null);
                        MedicalReportDto reportDto = report != null ? medicalReportService.getById(report.getReportId(),authHeader) : null;
                        List<String> services = appm.getServiceIds().stream().map(serviceId -> {
                            ClinicServices s = this.serviceRepository.getById(serviceId);
                            return s != null ? s.getServiceName() : null;
                        }).filter(Objects::nonNull).toList();
                        return appointmentMapper.getAppointment(appm, patient, doctor, services, reportDto, clinic.get().getClinicName());
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(Objects::nonNull).toList();
            } catch (Exception e) {
                throw new ClinicAppointmentException("Failed to retrieve appointments by patient ID " + id, e);
            }
        }
        throw new PatientException("You don't have access to this user!");
    }

    @Override
    public List<AppointmentDto> getAllByDoctor(Long id,String authHeader) throws ClinicAppointmentException {
        Doctor d = this.doctorRepository.findByEmail(this.authHeaderService.getEmail(authHeader)).orElse(null);
        if(d != null && Objects.equals(d.getDoctorId(), id)) {
            try {
                return appointmentRepository.findAllByDoctorId(id).stream().map(appm -> {
                    Patient patient = patientService.getById(appm.getPatientId());
                    Doctor doctor = this.doctorRepository.getById(appm.getDoctorId());
                    MedicalReport report = this.reportRepository.findByAppointmentId(appm.getAppointmentId()).orElse(null);
                    MedicalReportDto reportDto = null;
                    try {
                        reportDto = medicalReportService.getById(report.getReportId(),authHeader);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Optional<Clinic> clinic = clinicRepository.findByClinicId(appm.getClinicId());

                    List<String> services = appm.getServiceIds().stream().map(service -> {
                        ClinicServices s = this.serviceRepository.getById(service);
                        return s.getServiceName();
                    }).toList();
                    return appointmentMapper.getAppointment(appm, patient, doctor, services, reportDto, clinic.get().getClinicName());
                }).toList();
            } catch (Exception e) {
                throw new ClinicAppointmentException("Failed to retrieve appointments by doctor ID " + id, e);
            }
        }
            throw new PatientException("You don't have access to this user!");
    }

    @Override
    public Appointment save(CreateAppointmentDto appointment,String authHeader) {
        try {
            if(authHeader.isEmpty()) {
                Patient patient = this.patientRepository.findByEmail(authHeaderService.getEmail(authHeader));
                ClinicSchedule clinicSchedule = clinicScheduleRepository.findClinicScheduleByClinicIdAndStartTime(appointment.getClinicId(), appointment.getDate());

                CreateAppointmentDto createAppointmentDto = new CreateAppointmentDto(patient.getPatientId(), clinicSchedule.getDoctorId(), appointment.getClinicId(), appointment.getDate(),
                        appointment.getServiceId());
                return appointmentRepository.save(appointmentMapper.createAppointment(createAppointmentDto));
            }
            return appointmentRepository.save(appointmentMapper.createAppointment(appointment));
        } catch (DataAccessException e) {
            throw new ClinicAppointmentException("Failed to save appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public Appointment update(AppointmentDto appointment,String authHeader) throws ClinicAppointmentException {
        Patient p = this.patientRepository.findByEmail(this.authHeaderService.getEmail(authHeader));
        if(p.getPatientId() == appointment.getPatientId()) {
            try {
                Appointment old = this.appointmentRepository.findByAppointmentId(appointment.getAppointmentId());
                UpdatePatientDto patient = new UpdatePatientDto(appointment.getPatientId(), appointment.getPatientName(), appointment.getGender(), appointment.getAddress(), appointment.getContactNumber(), appointment.getEmail(), appointment.getBirthDay());
                patientService.update(patient,authHeader);
                return appointmentRepository.save(appointmentMapper.updateAppointment(old, appointment));
            } catch (Exception e) {
                throw new ClinicAppointmentException("Failed to update appointment: " + e.getMessage(), e);
            }
        }
        throw new PatientException("You don't have access to this user!");
    }

    @Override
    public void delete(Long id,String authHeader) throws Exception {
        Patient p = this.patientRepository.findByEmail(this.authHeaderService.getEmail(authHeader));
        Long clinicId = this.authHeaderService.getClinicId(authHeader);
        Appointment appointment = this.appointmentRepository.findByAppointmentId(id);

        if(Objects.equals(p.getPatientId(), appointment.getPatientId()) || Objects.equals(clinicId, appointment.getClinicId())) {
            try {
                ClinicSchedule clinicSchedule = this.clinicScheduleRepository.findClinicScheduleByClinicIdAndStartTime(appointment.getClinicId(), appointment.getDate());
                clinicSchedule.setIsBooked(false);
                this.clinicScheduleRepository.save(clinicSchedule);
                appointmentRepository.deleteById(id);
            } catch (EmptyResultDataAccessException e) {
                throw new ClinicAppointmentException("Appointment with ID " + id + " not found", e);
            } catch (Exception e) {
                throw new ClinicAppointmentException("Failed to delete appointment with ID " + id, e);
            }
        }
        throw new ClinicAppointmentException("You don't have access to delete this appointment!");
    }

    @Override
    public List<AppointmentDateDto> getAppointmentDates(String authHeader) throws Exception {
        Long clinicId = authHeaderService.getClinicId(authHeader);
        try {
            return this.appointmentRepository.findAllByClinicId(clinicId)
                    .stream()
                    .map(appm ->  new AppointmentDateDto(appm.getDate()))
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            throw new ClinicAppointmentException("Failed to retrieve appointment dates", e);
        }
    }

    @Override
    public Appointment createAppointmentByReceptionist(AppointmentByReceptionistDto dto,String authHeader) throws ClinicAppointmentException {
        Receptionist r = this.receptionistRepository.findByEmailAddress(this.authHeaderService.getEmail(authHeader)).orElse(null);

            try {
                CreatePatientDto createPatientDto = new CreatePatientDto(dto.getPatientName(), dto.getGender(), dto.getAddress(), dto.getContactNumber(), dto.getEmail(), dto.getBirthDay());
                Patient patient = patientService.save(createPatientDto);
                ClinicSchedule clinicSchedule = clinicScheduleRepository.findClinicScheduleByClinicIdAndStartTime(r.getClinicId(), dto.getAppointment());

                CreateAppointmentDto createAppointmentDto = new CreateAppointmentDto(patient.getPatientId(), clinicSchedule.getDoctorId(), r.getClinicId(), dto.getAppointment(), dto.getServiceId());
                clinicSchedule.setIsBooked(true);
                clinicScheduleRepository.save(clinicSchedule);
                return this.save(createAppointmentDto, "");
            } catch (Exception e) {
                throw new ClinicAppointmentException("Failed to create appointment", e);
            }

    }

    @Override
    public void changeAttended(Long id, boolean attended,String authHeader) throws ClinicAppointmentException {
        Receptionist r = this.receptionistRepository.findByEmailAddress(this.authHeaderService.getEmail(authHeader)).orElse(null);

        try {
            Appointment appointment = this.appointmentRepository.findByAppointmentId(id);
            if (r != null && Objects.equals(r.getClinicId(), appointment.getClinicId())) {

                appointment.setAttended(attended);
                appointmentRepository.save(appointment);
            }else{
                throw new ClinicAppointmentException("You don't have access for this changes");
            }

        } catch (Exception e) {
            throw new ClinicAppointmentException("Failed to change attended status for appointment with ID " + id, e);
        }
    }

    @Override
    public AppointmentDto nexAppointment(Long id,String authHeader) {
        Patient p = this.patientRepository.findByEmail(this.authHeaderService.getEmail(authHeader));

        if(p != null && Objects.equals(p.getPatientId(), id)) {
            return this.appointmentRepository.findAllByPatientId(id)
                    .stream().filter(appointment -> !appointment.isAttended())
                    .filter(appointment -> appointment.getDate().toLocalDate().isAfter(LocalDate.now())) // Filter appointments after today
                    .min(Comparator.comparing(Appointment::getDate))
                    .map(appm -> {
                        Patient patient = patientService.getById(appm.getPatientId());
                        Doctor doctor = this.doctorRepository.getById(appm.getDoctorId());
                        Optional<Clinic> clinic = clinicRepository.findByClinicId(appm.getClinicId());

                        List<String> services = appm.getServiceIds().stream().map(service -> {
                            ClinicServices s = this.serviceRepository.getById(service);
                            return s.getServiceName();
                        }).toList();
                        return appointmentMapper.getAppointment(appm, patient, doctor, services, null, clinic.get().getClinicName());
                    }).orElseThrow(() -> new ClinicAppointmentException("Doesn't have any next appointment"));
        }
        return null;
    }
}
