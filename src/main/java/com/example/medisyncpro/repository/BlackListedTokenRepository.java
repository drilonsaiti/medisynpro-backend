package com.example.medisyncpro.repository;
import com.example.medisyncpro.model.enums.TokenType;


import com.example.medisyncpro.model.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, Long> {
    boolean existsByTokenAndTokenType(String token, TokenType tokenType);
}