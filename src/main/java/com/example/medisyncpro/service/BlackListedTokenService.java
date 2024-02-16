package com.example.medisyncpro.service;
import com.example.medisyncpro.model.enums.TokenType;

public interface BlackListedTokenService {
    void blacklistToken(String token, TokenType tokenType);
    boolean isTokenBlacklisted(String token, TokenType tokenType);
}
