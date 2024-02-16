package com.example.medisyncpro.model.mapper;

import com.example.medisyncpro.model.Receptionist;
import com.example.medisyncpro.model.dto.CreateReceptionistDto;
import com.example.medisyncpro.model.dto.ReceptionistDto;
import org.springframework.stereotype.Service;

@Service
public class ReceptionistMapper {

    public Receptionist createReceptionist(CreateReceptionistDto dto) {
        return new Receptionist(dto.getReceptionistName(), dto.getEmailAddress(), dto.getClinicId());
    }

    public ReceptionistDto getReceptionistById(Receptionist receptionist, String clinicName) {
        return new ReceptionistDto(
                receptionist.getReceptionistId(),
                receptionist.getReceptionistName(),
                receptionist.getEmailAddress(),
                clinicName
        );

    }
}
