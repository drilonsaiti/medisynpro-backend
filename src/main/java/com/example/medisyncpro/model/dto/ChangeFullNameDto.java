package com.example.medisyncpro.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ChangeFullNameDto {
    String token;
    String fullName;
    String address;
    String gender;
    Long specializations;
    String education;
    String contactNumber;
    String password;
    LocalDate birthDay;
}
