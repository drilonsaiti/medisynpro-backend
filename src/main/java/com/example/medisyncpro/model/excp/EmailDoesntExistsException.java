package com.example.medisyncpro.model.excp;

public class EmailDoesntExistsException extends RuntimeException{
    public EmailDoesntExistsException() {
        super(String.format("Email doesn't exists"));
    }
}
