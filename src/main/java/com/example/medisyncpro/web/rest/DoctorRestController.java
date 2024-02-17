package com.example.medisyncpro.web.rest;


import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.dto.AddDoctorToClinicDto;
import com.example.medisyncpro.model.dto.CreateDoctorDto;
import com.example.medisyncpro.model.dto.DoctorResultDto;
import com.example.medisyncpro.model.dto.SearchDoctorDto;
import com.example.medisyncpro.model.excp.DoctorException;
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
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class DoctorRestController {

    private final DoctorService doctorService;
    private final SpecializationService specializationService;

    @GetMapping
    public ResponseEntity<?> listDoctors(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "all") String specializations,
                                         @RequestParam(defaultValue = "all") String service,
                                         HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            PageRequest pageable = PageRequest.of(page - 1, 15);
            DoctorResultDto clinicDtoList = doctorService.getAll(pageable, specializations, service,authHeader);

            return new ResponseEntity(new PageImpl<>(clinicDtoList.getClinics(), pageable, clinicDtoList.getTotalElements()), HttpStatus.OK);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<?> listDoctors(@PathVariable Long clinicId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "all") String specializations,
                                         @RequestParam(defaultValue = "all") String service) {
        try {
            PageRequest pageable = PageRequest.of(page - 1, 15);
            DoctorResultDto clinicDtoList = doctorService.getAllByClinicId(clinicId, pageable, specializations, service);

            return new ResponseEntity(new PageImpl<>(clinicDtoList.getClinics(), pageable, clinicDtoList.getTotalElements()), HttpStatus.OK);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getById(id);
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allDoctors/{clinicId}")
    public ResponseEntity<?> getAllDoctors(@PathVariable Long clinicId) {
        try {
            List<SearchDoctorDto> doctors = doctorService.getAllDoctors(clinicId);
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addDoctorToClinic/{clinicId}")
    public ResponseEntity<?> addDoctorToClinic(@PathVariable Long clinicId, @RequestBody List<AddDoctorToClinicDto> dto, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            doctorService.addDoctorToClinic(dto, clinicId,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody CreateDoctorDto dto) {
        try {
            doctorService.save(dto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            doctorService.update(doctor,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            doctorService.delete(id,authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/{clinicId}")
    public ResponseEntity<?> deleteDoctorFromClinic(@PathVariable Long id, @PathVariable Long clinicId, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            doctorService.deleteDoctorFromClinic(id, clinicId,authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getDoctorProfile(HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Doctor doctor = doctorService.getDoctorProfile(authHeader);
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        } catch (DoctorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}