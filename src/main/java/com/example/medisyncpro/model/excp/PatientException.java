package com.example.medisyncpro.model.excp;

public class PatientException extends RuntimeException {

    public PatientException(String message) {
        super(message);
    }

    public PatientException(String message, Throwable cause) {
        super(message, cause);
    }
}