package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentByReceptionistDto {
    String patientName;
    String gender;
    String address;
    String contactNumber;
    String email;
    LocalDate birthDay;

    LocalDateTime appointment;
    List<Long> serviceId;
    Long clinicId;
}
