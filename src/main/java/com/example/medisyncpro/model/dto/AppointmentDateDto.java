package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDateDto {
    LocalDateTime startDate;
    /*LocalDateTime endDate;*/
}
