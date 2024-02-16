package com.example.medisyncpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDto {

    String token;
    String oldPassword;
    String newPassword;
    String repeatedPassword;
}
