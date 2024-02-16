package com.example.medisyncpro.model.excp;

public class PasswordsDoNotMatchException extends RuntimeException{

    public PasswordsDoNotMatchException() {
        super("Passwords do not match.");
    }
}

