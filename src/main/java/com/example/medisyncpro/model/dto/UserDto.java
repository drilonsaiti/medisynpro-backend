package com.example.medisyncpro.model.dto;

import lombok.Data;

@Data
public class UserDto {

    String email;
    String password;

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
