package com.example.medisyncpro.model.enums;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {

    ROLE_PATIENT, ROLE_ADMIN,ROLE_RECEPTIONIST,ROLE_OWNER,ROLE_DOCTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
