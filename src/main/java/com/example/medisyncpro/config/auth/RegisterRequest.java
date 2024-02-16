package com.example.medisyncpro.config.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String fullName;

  private String email;

  private String password;

  private String repeatPassword;
  private String userType;

}
