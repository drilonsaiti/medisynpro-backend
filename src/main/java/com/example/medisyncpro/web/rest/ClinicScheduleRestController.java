package com.example.medisyncpro.web.rest;


import com.example.medisyncpro.model.ClinicSchedule;
import com.example.medisyncpro.model.dto.ClinicScheduleResultDto;
import com.example.medisyncpro.model.dto.CreateClinicSchedulesDto;
import com.example.medisyncpro.model.excp.ClinicScheduleException;
import com.example.medisyncpro.service.ClinicScheduleService;
import com.example.medisyncpro.service.DoctorService;
import com.example.medisyncpro.service.SettingsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clinicSchedules")
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class ClinicScheduleRestController {

    private final ClinicScheduleService clinicScheduleService;
    private final DoctorService doctorService;
    private final SettingsService service;

    @GetMapping
    public ResponseEntity<?> listClinicSchedules() {
        try {
            List<ClinicSchedule> clinicSchedules = clinicScheduleService.getAll();
            return new ResponseEntity<>(clinicSchedules, HttpStatus.OK);
        } catch (ClinicScheduleException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClinicSchedulesById(@PathVariable Long id) {
        try {
            ClinicSchedule clinicSchedule = clinicScheduleService.getById(id);
            return new ResponseEntity<>(clinicSchedule, HttpStatus.OK);
        } catch (ClinicScheduleException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createClinicSchedules(@RequestBody CreateClinicSchedulesDto dto, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");

            clinicScheduleService.save(dto, authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClinicScheduleException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateClinicSchedules(@RequestBody ClinicSchedule clinicServices, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");

            clinicScheduleService.update(clinicServices, authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClinicScheduleException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClinicSchedules(@PathVariable Long id, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");

            clinicScheduleService.delete(id, authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ClinicScheduleException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/generate/{id}")
    public ResponseEntity<?> generateClinicSchedules(@PathVariable Long id, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");

            Iterable<ClinicSchedule> clinicSchedules = clinicScheduleService.generateSchedules(id, authHeader);
            return new ResponseEntity<>(clinicSchedules, HttpStatus.OK);
        } catch (ClinicScheduleException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/grouped")
    public ResponseEntity<?> getAllByClinicGroupedByDate(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "startDate-asc") String sort,
            HttpServletRequest request) {
        try {
            PageRequest pageable = PageRequest.of(page - 1, 15);
            final String authHeader = request.getHeader("Authorization");
            ClinicScheduleResultDto results = clinicScheduleService.getAllByClinicGroupedByDate(pageable, sort, authHeader);
            return new ResponseEntity<>(new PageImpl<>(results.getClinicSchedule(), pageable, results.getTotalElements()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/grouped/{id}/{date}")
    public ResponseEntity<?> deleteClinicSchedulesGrouped(@PathVariable Long id, @PathVariable String date, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");

            clinicScheduleService.deleteGrouped(id, date, authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ClinicScheduleException | ParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> listClinicSchedulesByDoctor(@PathVariable Long doctorId, HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");

            List<ClinicSchedule> clinicSchedules = clinicScheduleService.getAllByDoctorId(doctorId, authHeader);

            return new ResponseEntity<>(clinicSchedules, HttpStatus.OK);
        } catch (ClinicScheduleException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
