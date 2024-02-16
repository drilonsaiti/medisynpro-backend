package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.Appointment;
import com.example.medisyncpro.model.ClinicServices;
import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.Patient;
import com.example.medisyncpro.model.dto.AppointmentDto;
import com.example.medisyncpro.model.dto.CreateAppointmentDto;
import com.example.medisyncpro.model.dto.MedicalReportDto;
import com.example.medisyncpro.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentMapper {
    private final ServiceRepository serviceRepository;

    public Appointment createAppointment(CreateAppointmentDto dto) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(dto.getPatientId());
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setClinicId(dto.getClinicId());
        appointment.setDate(dto.getDate());
        appointment.setAllServicesIds(dto.getServiceId());
        return appointment;
    }

    public Appointment updateAppointment(Appointment old, AppointmentDto newAppointment) {
        List<Long> servicesId = serviceRepository.findAll().stream()
                .filter(service -> newAppointment.getServiceName().contains(service.getServiceName()))
                .map(ClinicServices::getServiceId)
                .toList();
        old.setDate(newAppointment.getDate());
        old.getServiceIds().clear();
        old.getServiceIds().addAll(servicesId);
        old.setAttended(newAppointment.isAttended());
        return old;
    }

    public AppointmentDto getAppointment(Appointment appm, Patient patient, Doctor doctor, List<String> services, MedicalReportDto report, String clinicName) {
        return new AppointmentDto(
                appm.getAppointmentId(),
                appm.getDate(),
                patient.getPatientId(),
                patient.getPatientName(),
                patient.getEmail(),
                patient.getGender(),
                patient.getAddress(),
                patient.getContactNumber(),
                patient.getBirthDay(),
                doctor.getDoctorName(),
                doctor.getSpecialization().getSpecializationName(),
                doctor.getImageUrl(),
                appm.getClinicId(),
                clinicName,
                appm.getDate(),
                services,
                false,
                report
        );
    }

    /*public AppointmentDateDto getAppointmentDates(List<ClinicSchedule> list){
        return list.stream().map(data -> new AppointmentDateDto(data.getStartTime().toLocalDate()));
    }*/
}
