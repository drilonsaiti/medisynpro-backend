package com.example.medisyncpro.model.excp;

public class ReceptionistException extends RuntimeException {

    public ReceptionistException(String message) {
        super(message);
    }

    public ReceptionistException(String message, Throwable cause) {
        super(message, cause);
    }
}