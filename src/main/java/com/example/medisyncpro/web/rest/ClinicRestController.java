package com.example.medisyncpro.web.rest;


import com.example.medisyncpro.model.Clinic;
import com.example.medisyncpro.model.ClinicServices;
import com.example.medisyncpro.model.dto.ClinicDto;
import com.example.medisyncpro.model.dto.ClinicResultDto;
import com.example.medisyncpro.model.dto.UpdateClinicDto;
import com.example.medisyncpro.model.excp.ClinicException;
import com.example.medisyncpro.service.ClinicService;
import com.example.medisyncpro.service.DoctorService;
import com.example.medisyncpro.service.SpecializationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clinics")
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class ClinicRestController {

    private final ClinicService clinicService;
    private final SpecializationService specializationService;
    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<?> listClinics(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "all") String specializations,
                                         @RequestParam(defaultValue = "all") String service,
                                         @RequestParam(defaultValue = "all") String byDate) {
        try {
            PageRequest pageable = PageRequest.of(page - 1, 15);
            ClinicResultDto clinicDtoList = clinicService.getAll(pageable, specializations, service, byDate);
            return new ResponseEntity<>(new PageImpl<>(clinicDtoList.getClinics(), pageable, clinicDtoList.getTotalElements()), HttpStatus.OK);
        } catch (ClinicException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ClinicDto clinicDto = clinicService.getByIdDto(id);
            return new ResponseEntity<>(clinicDto, HttpStatus.OK);
        } catch (ClinicException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/myProfile")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            ClinicDto clinicDto = clinicService.getMyProfile(authHeader);
            return new ResponseEntity<>(clinicDto, HttpStatus.OK);
        } catch (ClinicException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<?> getClinicServiceById(@PathVariable Long id, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            List<ClinicServices> clinicServices = clinicService.getClinicServicesById(id,authHeader);
            return new ResponseEntity<>(clinicServices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateClinic(@RequestBody UpdateClinicDto dto, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Clinic updatedClinic = clinicService.updateClinic(dto,authHeader);
            return new ResponseEntity<>(updatedClinic, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClinic(@PathVariable Long id, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            clinicService.delete(id,authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ClinicException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}