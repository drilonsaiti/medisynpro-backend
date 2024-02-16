package com.example.medisyncpro.model.excp;

public class ClinicAppointmentException extends RuntimeException {

    public ClinicAppointmentException(String message) {
        super(message);
    }

    public ClinicAppointmentException(String message, Throwable cause) {
        super(message, cause);
    }
}