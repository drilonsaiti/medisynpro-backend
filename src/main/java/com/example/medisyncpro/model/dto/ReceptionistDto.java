package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceptionistDto {
    private Long receptionistId;

    private String receptionistName;

    private String emailAddress;

    private String clinicName;
}
