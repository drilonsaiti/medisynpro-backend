package com.example.medisyncpro.model.excp;

public class ClinicScheduleException extends RuntimeException {

    public ClinicScheduleException(String message) {
        super(message);
    }

    public ClinicScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}