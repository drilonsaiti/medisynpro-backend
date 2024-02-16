package com.example.medisyncpro.model.dto;

import lombok.Data;

@Data
public class ChangeFullNameDto {
    String token;
    String fullName;
    String address;
    String specializationId;
    String education;
    String contactNumber;
    String password;
}
