package com.example.medisyncpro.model.excp;

public class DoctorException extends RuntimeException {

    public DoctorException(String message) {
        super(message);
    }

    public DoctorException(String message, Throwable cause) {
        super(message, cause);
    }
}