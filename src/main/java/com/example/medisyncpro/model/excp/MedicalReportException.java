package com.example.medisyncpro.model.excp;

public class MedicalReportException extends RuntimeException {

    public MedicalReportException(String message) {
        super(message);
    }

    public MedicalReportException(String message, Throwable cause) {
        super(message, cause);
    }
}