package com.example.medisyncpro.model.excp;

public class ClinicException extends RuntimeException {

    public ClinicException(String message) {
        super(message);
    }

    public ClinicException(String message, Throwable cause) {
        super(message, cause);
    }
}