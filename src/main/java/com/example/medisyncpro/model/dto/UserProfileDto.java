package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {

    String email;
    String profileImage;
    String fullName = "";
    String address = "";
    String gender = "";
    Long specializationId =null;
    String education = "";
    String contactNumber = "";
    String password = "";
    LocalDate birthDay = null;


    public UserProfileDto(String email, String imageUrl, String fullName, String education, Long specializationId) {
        this.email = email;
        this.profileImage = imageUrl;
        this.fullName = fullName;
        this.specializationId = specializationId;
        this.education = education;
    }

    public UserProfileDto(String email,String imageUrl, String fullName, String address, String gender, String contactNumber, LocalDate birthDay) {
        this.email = email;
        this.profileImage = imageUrl;
        this.fullName = fullName;
        this.address = address;
        this.gender  = gender;
        this.contactNumber = contactNumber;
        this.birthDay = birthDay;
    }

    public UserProfileDto(String email, String imageUrl, String fullName) {
        this.email = email;
        this.profileImage = imageUrl;
        this.fullName = fullName;
    }
}
