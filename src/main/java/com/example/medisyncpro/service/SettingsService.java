package com.example.medisyncpro.service;

import com.example.medisyncpro.model.Settings;
import com.example.medisyncpro.model.dto.SettingsDTO;

import java.util.List;

public interface SettingsService {
    List<SettingsDTO> getAllSettings();

    Settings getSettingsById(String authHeader) throws Exception;

    Settings saveSettings(Settings settings,String authHeader) throws Exception;

    SettingsDTO updateSettings(SettingsDTO settings,String authHeader) throws Exception;

    void deleteSettings(String authHeader) throws Exception;
}