package com.example.medisyncpro.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateReceptionistDto {

    private String receptionistName;


    private String emailAddress;


    private Long clinicId;

    public CreateReceptionistDto( String emailAddress,String receptionistName) {
        this.receptionistName = receptionistName;
        this.emailAddress = emailAddress;
        this.clinicId = null;
    }
}
