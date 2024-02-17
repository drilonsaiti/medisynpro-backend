package com.example.medisyncpro.web.rest;

import com.example.medisyncpro.model.Appointment;
import com.example.medisyncpro.model.dto.*;
import com.example.medisyncpro.model.excp.ClinicAppointmentException;
import com.example.medisyncpro.model.excp.MedicalReportException;
import com.example.medisyncpro.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<?> listAppointments(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "all") String nameOrEmail,
                                              @RequestParam(defaultValue = "all") String types,
                                              HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        PageRequest pageable = PageRequest.of(page - 1, 15);
        AppointmentResultDto appointments = appointmentService.getAll(pageable, nameOrEmail, types,authHeader);
        return new ResponseEntity<>(new PageImpl<>(appointments.getAppointments(), pageable, appointments.getTotalElements()), HttpStatus.OK);

    }

    @GetMapping("/myAppointment")
    public ResponseEntity<?> getMyAppointment(@RequestParam(defaultValue = "0") int page,
                                                       HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            PageRequest pageable = PageRequest.of(page - 1, 15);
            AppointmentResultDto report = appointmentService.getMyAppointment(pageable,authHeader);
            return new ResponseEntity<>(new PageImpl<>(report.getAppointments(), pageable, report.getTotalElements()), HttpStatus.OK);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Appointment appointment = appointmentService.getById(id,authHeader);
            return new ResponseEntity<>(appointment, HttpStatus.OK);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getAppointmentByPatient(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Iterable<AppointmentDto> appointments = appointmentService.getAllByPatient(id,authHeader);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<?> getAppointmentByDoctor(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Iterable<AppointmentDto> appointments = appointmentService.getAllByDoctor(id,authHeader);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<?> createAppointments(@RequestBody CreateAppointmentDto dto,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            appointmentService.save(dto,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateAppointments(@RequestBody AppointmentDto appointments,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");

            appointmentService.update(appointments,authHeader);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointments(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");

            appointmentService.delete(id,authHeader);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dates")
    public ResponseEntity<?> getAppointmentDates(HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            Iterable<AppointmentDateDto> appointments = appointmentService.getAppointmentDates(authHeader);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/byReceptionist")
    public ResponseEntity<?> createAppointmentByReceptionist(@RequestBody AppointmentByReceptionistDto dto,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            appointmentService.createAppointmentByReceptionist(dto,authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/changeAttended/{id}")
    public ResponseEntity<?> changeAttended(@PathVariable Long id, @RequestBody ChangeAttendedDto attended,
                                            HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            appointmentService.changeAttended(id, attended.isAttended(),authHeader);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/nextAppointment/{id}")
    public ResponseEntity<?> nextAppointment(@PathVariable Long id,HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            AppointmentDto next = appointmentService.nexAppointment(id,authHeader);
            return new ResponseEntity<>(next, HttpStatus.CREATED);
        } catch (ClinicAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
