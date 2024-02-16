package com.example.medisyncpro.model;
import com.example.medisyncpro.model.enums.TokenType;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_tokens")
@Data
public class BlackListedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    private TokenType tokenType;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt = LocalDateTime.now();
}