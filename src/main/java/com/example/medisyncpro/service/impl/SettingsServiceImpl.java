package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.Settings;
import com.example.medisyncpro.model.dto.SettingsDTO;
import com.example.medisyncpro.model.excp.ReceptionistException;
import com.example.medisyncpro.model.excp.SettingsException;
import com.example.medisyncpro.model.mapper.SettingsMapper;
import com.example.medisyncpro.repository.SettingsRepository;
import com.example.medisyncpro.service.AuthHeaderService;
import com.example.medisyncpro.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;
    private final SettingsMapper settingsMapper;
    private final AuthHeaderService authHeaderService;

    @Override
    public List<SettingsDTO> getAllSettings() {
        try {
            return settingsRepository.findAll().stream().map(settings -> settingsMapper.toDTO(settings)).toList();
        } catch (Exception e) {
            throw new SettingsException("Error retrieving all settings");
        }
    }

    @Override
    public Settings getSettingsById(String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

            try {
                return settingsRepository.findByClinicId(clinicIdAuth)
                        .orElseThrow(() -> new SettingsException("Settings not found with id: " + clinicIdAuth));
            } catch (Exception e) {
                throw new SettingsException("Error retrieving settings by id: " + clinicIdAuth, e);
            }

    }

    @Override
    public Settings saveSettings(Settings settings, String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(settings.getClinicId(), clinicIdAuth)) {
        try {
            return settingsRepository.save(settings);
        } catch (Exception e) {
            throw new SettingsException("Error saving settings", e);
        }
        }
        throw new ReceptionistException("You don't have access");
    }

    @Override
    public SettingsDTO updateSettings(SettingsDTO dto, String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(dto.getClinicId(), clinicIdAuth)) {
        try {
            Settings settings = settingsRepository.findById(dto.getId())
                    .orElseThrow(() -> new SettingsException("Settings not found with id: " + dto.getId()));
            Settings updatedSettings = settingsMapper.updateSettings(dto, settings);
            settingsRepository.save(updatedSettings);
            return dto;
        } catch (Exception e) {
            throw new SettingsException("Error updating settings", e);
        }
        }
        throw new ReceptionistException("You don't have access");
    }

    @Override
    public void deleteSettings(String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);
        Settings settings = this.settingsRepository.findByClinicId(clinicIdAuth).orElse(null);
        if (settings != null) {
            try {
                settingsRepository.deleteById(settings.getId());
            } catch (Exception e) {
                throw new SettingsException("Error deleting settings", e);
            }
        }
    }
}
