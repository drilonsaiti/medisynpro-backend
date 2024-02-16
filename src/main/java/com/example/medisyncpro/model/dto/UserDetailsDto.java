package com.example.medisyncpro.model.dto;



import com.example.medisyncpro.model.User;
import com.example.medisyncpro.model.enums.Role;
import lombok.Data;

@Data
public class UserDetailsDto {
    private String email;
    private Role role;

    public static UserDetailsDto of(User user) {
        UserDetailsDto details = new UserDetailsDto();
        details.email = user.getEmail();
        details.role = user.getRole();
        return details;
    }
}