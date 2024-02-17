package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.MedicalReport;
import com.example.medisyncpro.model.dto.CreateMedicalReportDto;
import com.example.medisyncpro.model.dto.MedicalReportDto;
import com.example.medisyncpro.model.dto.ServiceDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalReportMapper {


    public MedicalReportDto getMedicalReport(MedicalReport report, Long patientId, String patientName, String patientEmail, List<ServiceDto> services, int totalPrice) {
        return new MedicalReportDto(
                report.getReportId(),
                report.getDisease(),
                report.getMedicineName(),
                report.getNextAppointmentDate(),
                report.getReportDate(),
                patientId,
                patientName,
                patientEmail,
                report.getDoctor().getDoctorName(),
                report.getDoctor().getDoctorId(),
                report.getDoctor().getEmail(),
                report.getAppointmentDate(),
                services,
                totalPrice
        );
    }

    public MedicalReport createMedicalReport(CreateMedicalReportDto dto, Doctor doctor, LocalDateTime date,Long patientId,Long clinicId) {
        return new MedicalReport(
                patientId,
                clinicId,
                doctor,
                dto.getMedicineName(),
                dto.getDisease(),
                dto.getNextAppointmentDate(),
                dto.getNoOfDays(),
                date,
                dto.getAppointmentId()
        );
    }

    public MedicalReport updateMedicalReport(MedicalReport old, MedicalReport newReport) {
        old.setMedicineName(newReport.getMedicineName());
        old.setDisease(newReport.getDisease());
        old.setNextAppointmentDate(newReport.getNextAppointmentDate());
        old.setNoOfDays(newReport.getNoOfDays());
        old.setReportDate(newReport.getReportDate());

        return old;
    }
}
