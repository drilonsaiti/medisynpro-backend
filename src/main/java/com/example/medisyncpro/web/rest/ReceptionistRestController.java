package com.example.medisyncpro.web.rest;


import com.example.medisyncpro.model.Receptionist;
import com.example.medisyncpro.model.dto.AddDoctorToClinicDto;
import com.example.medisyncpro.model.dto.AddReceptionistToClinicDto;
import com.example.medisyncpro.model.dto.CreateReceptionistDto;
import com.example.medisyncpro.model.dto.SearchReceptionistDto;
import com.example.medisyncpro.model.excp.DoctorException;
import com.example.medisyncpro.model.excp.ReceptionistException;
import com.example.medisyncpro.service.ClinicService;
import com.example.medisyncpro.service.DoctorService;
import com.example.medisyncpro.service.ReceptionistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/receptionists")
@CrossOrigin
public class ReceptionistRestController {

    private final ReceptionistService receptionistService;
    private final DoctorService doctorService;
    private final ClinicService clinicService;

    @GetMapping
    public ResponseEntity<?> listReceptionists() {
        Iterable<Receptionist> receptionists = receptionistService.getAll();
        return new ResponseEntity<>(receptionists, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReceptionistById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(receptionistService.getByIdDto(id), HttpStatus.OK);
        } catch (ReceptionistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<?> listReceptionist(@PathVariable Long clinicId,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Iterable<Receptionist> receptionists = receptionistService.getAllByClinicId(clinicId,authHeader);
            return new ResponseEntity<>(receptionists, HttpStatus.OK);
        } catch (ReceptionistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/search/{clinicId}")
    public ResponseEntity<?> listReceptionistsForSearch(@PathVariable Long clinicId,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Iterable<SearchReceptionistDto> receptionists = receptionistService.getAllReceptionistSearch(clinicId,authHeader);
            return new ResponseEntity<>(receptionists, HttpStatus.OK);
        } catch (ReceptionistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createReceptionist(@RequestBody CreateReceptionistDto dto) {
        try {
            this.receptionistService.save(dto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ReceptionistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateReceptionist(@RequestBody Receptionist receptionist,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            receptionistService.update(receptionist,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ReceptionistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReceptionist(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            receptionistService.delete(id,authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ReceptionistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/clinic/{id}")
    public ResponseEntity<?> addDoctorToClinic(@PathVariable Long id, @RequestBody List<AddReceptionistToClinicDto> dto, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            receptionistService.addReceptionistToClinic(dto, id,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/clinic/{id}")
    public ResponseEntity<?> deleteReceptionistFromClinic(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            receptionistService.deleteReceptionistFromClinic(id,authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ReceptionistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
