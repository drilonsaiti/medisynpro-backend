package com.example.medisyncpro.web.rest;

import com.example.medisyncpro.model.Patient;
import com.example.medisyncpro.model.dto.CreatePatientDto;
import com.example.medisyncpro.model.dto.PatientResultDto;
import com.example.medisyncpro.model.dto.UpdatePatientDto;
import com.example.medisyncpro.model.excp.PatientException;
import com.example.medisyncpro.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/patients")
@CrossOrigin
public class PatientRestController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<?> listPatients(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "all") String nameOrEmail,
                                          HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            PageRequest pageable = PageRequest.of(page - 1, 15);
            PatientResultDto patients = patientService.getAll(pageable, nameOrEmail,authHeader);
            return new ResponseEntity<>(new PageImpl<>(patients.getPatients(), pageable, patients.getTotalElements()), HttpStatus.OK);
        } catch (PatientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            Patient patient = patientService.getById(id);
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } catch (PatientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getPatientProfile(HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Patient patient = patientService.getPatientProfile(authHeader);
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } catch (PatientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody CreatePatientDto dto) {
        try {
            patientService.save(dto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (PatientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping
    public ResponseEntity<?> updatePatient(@RequestBody UpdatePatientDto patient,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            patientService.update(patient,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (PatientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            patientService.delete(id,authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (PatientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
