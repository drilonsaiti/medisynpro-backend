package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClinicScheduleResultDto {
    private List<GroupedClinicSchedule> clinicSchedule;
    private int totalElements;
}
