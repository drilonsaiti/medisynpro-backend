package com.example.medisyncpro.model.excp;

public class PasswordIncorrectExecption extends RuntimeException{

    public PasswordIncorrectExecption() {
        super("Password incorrect.");
    }
}