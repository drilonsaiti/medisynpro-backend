package com.example.medisyncpro.web.rest;

import com.example.medisyncpro.model.Specializations;
import com.example.medisyncpro.model.dto.CreateSpecializationDto;
import com.example.medisyncpro.model.excp.SpecializationException;
import com.example.medisyncpro.service.SpecializationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/specializations")
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class SpecializationRestController {

    private final SpecializationService specializationService;

    @GetMapping
    public ResponseEntity<?> listSpecializations() {
        try {
            Iterable<Specializations> specializations = specializationService.getAll();
            return new ResponseEntity<>(specializations, HttpStatus.OK);
        } catch (SpecializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecializationById(@PathVariable Long id) {
        try {
            Specializations specialization = specializationService.getById(id);
            return new ResponseEntity<>(specialization, HttpStatus.OK);
        } catch (SpecializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createSpecialization(@RequestBody CreateSpecializationDto dto) {
        try {
            specializationService.save(dto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (SpecializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateSpecialization(@RequestBody Specializations specializations) {
        try {
            specializationService.update(specializations);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (SpecializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpecialization(@PathVariable Long id) {
        try {
            specializationService.delete(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (SpecializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
