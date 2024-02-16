package com.example.medisyncpro.web.rest;

import com.example.medisyncpro.model.MedicalReport;
import com.example.medisyncpro.model.dto.CreateMedicalReportDto;
import com.example.medisyncpro.model.dto.MedicalReportResultDto;
import com.example.medisyncpro.model.excp.MedicalReportException;
import com.example.medisyncpro.service.MedicalReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/medicalReports")
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class MedicalReportRestController {

    private final MedicalReportService medicalReportService;

    @GetMapping
    public ResponseEntity<?> listMedicalReports(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "all") String nameOrEmail,
                                                @RequestParam(defaultValue = "all") String byDate,
                                                HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            PageRequest pageable = PageRequest.of(page - 1, 15);
            MedicalReportResultDto report = medicalReportService.getAll(pageable, nameOrEmail, byDate,authHeader);
            return new ResponseEntity(new PageImpl<>(report.getMedicalReport(), pageable, report.getTotalElements()), HttpStatus.OK);
        } catch (MedicalReportException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalReportById(@PathVariable Long id,
                                                  HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            return new ResponseEntity<>(medicalReportService.getById(id,authHeader), HttpStatus.OK);
        } catch (MedicalReportException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/myReports")
    public ResponseEntity<?> getMedicalReportByPatient(@RequestParam(defaultValue = "0") int page,
                                                  HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            PageRequest pageable = PageRequest.of(page - 1, 15);
            MedicalReportResultDto report = medicalReportService.getMedicalReportByPatient(pageable,authHeader);
            return new ResponseEntity<>(new PageImpl<>(report.getMedicalReport(), pageable, report.getTotalElements()), HttpStatus.OK);
        } catch (MedicalReportException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createMedicalReport(@RequestBody CreateMedicalReportDto dto,
                                                 HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            this.medicalReportService.save(dto,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MedicalReportException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateMedicalReport(@RequestBody MedicalReport patient,
                                                 HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            medicalReportService.update(patient,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MedicalReportException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicalReport(@PathVariable Long id,
                                                 HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            medicalReportService.delete(id,authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (MedicalReportException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
