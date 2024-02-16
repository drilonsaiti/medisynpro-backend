package com.example.medisyncpro.model.excp;

public class VerificationCodeDoNotMatchException extends RuntimeException{

    public VerificationCodeDoNotMatchException() {
        super("Verification code do not match.");
    }
}
