package com.example.medisyncpro.model.excp;

public class ClinicServicesException extends RuntimeException {

    public ClinicServicesException(String message) {
        super(message);
    }

    public ClinicServicesException(String message, Throwable cause) {
        super(message, cause);
    }
}