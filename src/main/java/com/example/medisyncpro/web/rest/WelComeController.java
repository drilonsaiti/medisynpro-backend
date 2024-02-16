/*
package com.example.medisyncpro.web.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class WelComeController {

    @GetMapping("/login")
    public String greeting(Authentication authentication) {

        String userName = authentication.getName();

        return "Spring Security In-memory Authentication Example - Welcome " + userName;
    }
}*/
