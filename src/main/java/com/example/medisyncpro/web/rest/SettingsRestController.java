package com.example.medisyncpro.web.rest;

import com.example.medisyncpro.model.Settings;
import com.example.medisyncpro.model.dto.SettingsDTO;
import com.example.medisyncpro.model.excp.SettingsException;
import com.example.medisyncpro.service.SettingsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class SettingsRestController {

    private final SettingsService settingsService;

    @GetMapping
    public ResponseEntity<?> getAllSettings() {
        try {
            List<SettingsDTO> settings = settingsService.getAllSettings();
            return new ResponseEntity<>(settings, HttpStatus.OK);
        } catch (SettingsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id")
    public ResponseEntity<?> getSettingsById(HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Settings settings = settingsService.getSettingsById(authHeader);
            return new ResponseEntity<>(settings, HttpStatus.OK);
        } catch (SettingsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createSettings(@RequestBody Settings settings,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Settings createdSettings = settingsService.saveSettings(settings,authHeader);
            return new ResponseEntity<>(createdSettings, HttpStatus.CREATED);
        } catch (SettingsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSettings(@PathVariable Long id, @RequestBody SettingsDTO settings,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            SettingsDTO updatedSettings = settingsService.updateSettings(settings,authHeader);
            return new ResponseEntity<>(updatedSettings, HttpStatus.OK);
        } catch (SettingsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSettings(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            settingsService.deleteSettings(authHeader);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (SettingsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
