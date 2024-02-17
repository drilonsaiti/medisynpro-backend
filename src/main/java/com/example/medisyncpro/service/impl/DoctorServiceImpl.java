package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.Clinic;
import com.example.medisyncpro.model.Doctor;
import com.example.medisyncpro.model.Specializations;
import com.example.medisyncpro.model.dto.AddDoctorToClinicDto;
import com.example.medisyncpro.model.dto.CreateDoctorDto;
import com.example.medisyncpro.model.dto.DoctorResultDto;
import com.example.medisyncpro.model.dto.SearchDoctorDto;
import com.example.medisyncpro.model.excp.ClinicException;
import com.example.medisyncpro.model.excp.DoctorException;
import com.example.medisyncpro.model.mapper.DoctorMapper;
import com.example.medisyncpro.repository.ClinicRepository;
import com.example.medisyncpro.repository.DoctorRepository;
import com.example.medisyncpro.repository.SpecializationRepository;
import com.example.medisyncpro.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorMapper doctorMapper;
    private final AuthHeaderServiceImpl authHeaderService;

    @Override
    public Doctor getById(Long id) {
        try {
            return doctorRepository.findById(id)
                    .orElseThrow(() -> new DoctorException("Doctor not found with ID: " + id));
        } catch (Exception e) {
            throw new DoctorException("Error getting doctor by ID", e);
        }
    }

    @Override
    public DoctorResultDto getAll(PageRequest pageable, String specializations, String service,String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        try {
            String[] specs = specializations.split(",");
            String[] services = service.split(",");

            List<Doctor> doctors = doctorRepository.findAll()
                    .stream()
                    .filter(d -> Objects.equals(d.getClinic().getClinicId(), clinicIdAuth))
                    .filter(doctor ->
                            (specializations.equals("all") || Arrays.asList(specs).contains(doctor.getSpecialization().getSpecializationName()))

                    ).toList();

            int totalElements = doctors.size();
            return new DoctorResultDto(doctors
                    .stream()
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize()).toList(), totalElements
            );
        } catch (Exception e) {
            throw new DoctorException("Error getting all doctors", e);
        }
    }

    @Override
    public DoctorResultDto getAllByClinicId(Long clinicId, PageRequest pageable, String specializations, String service) {
        try {
            String[] specs = specializations.split(",");
            String[] services = service.split(",");

            List<Doctor> doctors = doctorRepository.findAll()
                    .stream()
                    .filter(doctor -> doctor.getClinic() != null)
                    .filter(doctor -> Objects.equals(doctor.getClinic().getClinicId(), clinicId))
                    .filter(doctor ->
                            (specializations.equals("all") || Arrays.asList(specs).contains(doctor.getSpecialization().getSpecializationName()))
                    )
                    .toList();
            int totalElements = doctors.size();
            return new DoctorResultDto(doctors
                    .stream()
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize()).toList(), totalElements
            );
        } catch (Exception e) {
            throw new DoctorException("Error getting all doctors by clinic ID " + clinicId);
        }
    }

    @Override
    public List<SearchDoctorDto> getAllDoctors(Long clinicId){
        try {
            return doctorRepository.findAll()
                    .stream()
                    .filter(doctor -> {
                        Clinic clinic = doctor.getClinic();
                        return clinic == null || !Objects.equals(clinic.getClinicId(), clinicId);
                    })
                    .map(doctor -> new SearchDoctorDto(doctor.getEmail(), doctor.getDoctorName()))
                    .toList();
        } catch (Exception e) {
            throw new DoctorException("Error getting all doctors by clinic ID " + clinicId);
        }

    }

    @Override
    public Doctor save(CreateDoctorDto doctor) {
        try {
            Specializations specializations = null;
            Clinic clinic = null;

            if (doctor.getSpecializationId() != null) {
                specializations = specializationRepository.getById(doctor.getSpecializationId());
            }

            if (doctor.getClinicId() != null) {
                clinic = clinicRepository.getById(doctor.getClinicId());
            }
            return doctorRepository.save(doctorMapper.createDoctor(doctor, specializations, clinic));
        } catch (Exception e) {
            throw new DoctorException("Error saving doctor", e);
        }
    }

    @Override
    public Doctor update(Doctor doctor,String authHeader) throws Exception {
        Doctor d = this.doctorRepository.findByEmail(this.authHeaderService.getEmail(authHeader)).orElse(null);

        if(d != null && Objects.equals(d.getDoctorId(), doctor.getDoctorId())) {
            try {
                Doctor old = this.getById(doctor.getDoctorId());
                return doctorRepository.save(doctorMapper.updateDoctor(old, doctor));
            } catch (Exception e) {
                throw new DoctorException("Error updating doctor", e);
            }
        }
        throw new Exception("You don't have access!");

    }

    @Override
    public void delete(Long id,String authHeader) throws Exception {
        Doctor d = this.doctorRepository.findByEmail(this.authHeaderService.getEmail(authHeader)).orElse(null);

        if(d != null && Objects.equals(d.getDoctorId(), id)) {
        try {
            doctorRepository.deleteById(id);
        } catch (Exception e) {
            throw new DoctorException("Error deleting doctor", e);
        }
        }
        throw new Exception("You don't have access!");
    }

    @Override
    public void deleteDoctorFromClinic(Long id, Long clinicId,String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(clinicIdAuth, clinicId)) {
        try {
            Clinic clinic = clinicRepository.getById(clinicId);
            Doctor doctor = this.getById(id);
            clinic.getDoctors().remove(doctor);
            doctor.setClinic(null);
            clinicRepository.save(clinic);
            doctorRepository.save(doctor);
        } catch (Exception e) {
            throw new DoctorException("Error deleting doctor from clinic", e);
        }}
        throw new Exception("You don't have access!");
    }

    @Override
    public void addDoctorToClinic(List<AddDoctorToClinicDto> dto, Long clinicId,String authHeader) throws Exception {
        Long clinicIdAuth = this.authHeaderService.getClinicId(authHeader);

        if (Objects.equals(clinicIdAuth, clinicId)) {
        try {
            for (AddDoctorToClinicDto addDoctorToClinicDto : dto) {
                Doctor doctor = doctorRepository.findByEmail(addDoctorToClinicDto.getDoctorEmail())
                        .orElseThrow(() -> new DoctorException(String.format("Doctor with email: %s not found", addDoctorToClinicDto.getDoctorEmail())));
                Clinic clinic = clinicRepository.findByClinicId(clinicId)
                        .orElseThrow(() -> new ClinicException(String.format("Clinic with id: %d not found", clinicId)));

                doctor.setClinic(clinic);
                doctorRepository.save(doctor);
                clinic.getDoctors().add(doctor);
                clinicRepository.save(clinic);
            }
        } catch (Exception e) {
            throw new DoctorException("Error adding doctor to clinic: " + e.getMessage());
        }}
        throw new Exception("You don't have access!");
    }

    @Override
    public Doctor getDoctorProfile(String authHeader) throws Exception {
        String email = this.authHeaderService.getEmail(authHeader);
        return this.doctorRepository.findByEmail(email).orElse(null);
    }
}
