package com.example.medisyncpro.model.excp;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String username) {
        super(String.format("User with email: %s already exists", username));
    }
}

