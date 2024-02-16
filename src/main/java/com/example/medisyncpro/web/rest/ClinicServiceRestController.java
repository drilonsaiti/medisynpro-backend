package com.example.medisyncpro.web.rest;


import com.example.medisyncpro.model.ClinicServices;
import com.example.medisyncpro.model.dto.ClinicServicesResultDto;
import com.example.medisyncpro.model.dto.CreateClinicServicesDto;
import com.example.medisyncpro.model.excp.ClinicServicesException;
import com.example.medisyncpro.service.ClinicServicesService;
import com.example.medisyncpro.service.SpecializationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clinic-services")
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class ClinicServiceRestController {

    private final ClinicServicesService clinicServicesService;
    private final SpecializationService specializationService;


    @GetMapping
    public ResponseEntity<?> listServices(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "all") String specializations,
                                          @RequestParam(defaultValue = "id-asc") String sort) {
        try {
            PageRequest pageable = PageRequest.of(page - 1, 15);
            ClinicServicesResultDto services = clinicServicesService.getAll(pageable, specializations, sort);
            return new ResponseEntity<>(new PageImpl<>(services.getServices(), pageable, services.getTotalElements()), HttpStatus.OK);
        } catch (ClinicServicesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClinicServiceById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(clinicServicesService.getById(id), HttpStatus.OK);
        } catch (ClinicServicesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clinic")
    public ResponseEntity<?> getClinicServiceForClinic() {
        try {
            return new ResponseEntity<>(clinicServicesService.getClinicServiceForClinic(), HttpStatus.OK);
        } catch (ClinicServicesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<?> createClinicServices(@RequestBody CreateClinicServicesDto dto) {
        try {
            this.clinicServicesService.save(dto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClinicServicesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateClinicServices(@RequestBody ClinicServices clinicServices) {
        try {
            clinicServicesService.update(clinicServices);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClinicServicesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClinicServices(@PathVariable Long id) {
        try {
            clinicServicesService.delete(id);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ClinicServicesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

