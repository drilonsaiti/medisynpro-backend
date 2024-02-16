package com.example.medisyncpro.service.impl;

import com.example.medisyncpro.model.ClinicServices;
import com.example.medisyncpro.model.Specializations;
import com.example.medisyncpro.model.dto.ClinicServicesResultDto;
import com.example.medisyncpro.model.dto.CreateClinicServicesDto;
import com.example.medisyncpro.model.dto.ServiceForClinicsDto;
import com.example.medisyncpro.model.excp.ClinicException;
import com.example.medisyncpro.model.excp.ClinicScheduleException;
import com.example.medisyncpro.model.excp.ClinicServicesException;
import com.example.medisyncpro.model.mapper.ClinicServicesMapper;
import com.example.medisyncpro.repository.ServiceRepository;
import com.example.medisyncpro.repository.SpecializationRepository;
import com.example.medisyncpro.service.ClinicServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicServicesServiceImpl implements ClinicServicesService {


    private final ServiceRepository serviceRepository;
    private final SpecializationRepository specializationRepository;
    private final ClinicServicesMapper servicesMapper;

    @Override
    public ClinicServices getById(Long id) {
        try {
            return serviceRepository.findById(id).orElseThrow(() -> new ClinicScheduleException("Clinic service with ID " + id + " not found"));
        } catch (Exception e) {
            throw new ClinicException("Failed to retrieve clinic service by ID", e);
        }
    }

    private Comparable getFieldValue(
            ClinicServices booking, String field) {
        switch (field) {
            case "id":
                return booking.getServiceId();
            case "name":
                return booking.getServiceName();
            case "price":
                return booking.getPrice();
            case "duration":
                return booking.getDurationMinutes();
            default:
                throw new IllegalArgumentException("Invalid field for sorting: " + field);
        }
    }

    @Override
    public ClinicServicesResultDto getAll(PageRequest pageable, String specialization, String sort) {
        try {
            String[] specs = specialization.split(",");
            List<ClinicServices> services = serviceRepository.findAll().stream().filter(service ->
                            (specialization.equals("all") || Arrays.asList(specs).contains(service.getSpecializations().getSpecializationName())))
                    .sorted((a, b) -> {
                        String[] sortBy = sort.split("-");
                        String field = sortBy[0];
                        String direction = sortBy[1];
                        Comparable fieldA = getFieldValue(a, field);
                        Comparable fieldB = getFieldValue(b, field);

                        return direction.equals("asc") ? fieldA.compareTo(fieldB) : fieldB.compareTo(fieldA);
                    }).toList();

            return new ClinicServicesResultDto(services.stream().skip(pageable.getOffset()).limit(pageable.getPageSize()).toList(), services.size());
        } catch (Exception e) {
            throw new ClinicException("Failed to retrieve all clinics services", e);
        }
    }

    @Override
    public ClinicServices save(CreateClinicServicesDto clinicServices) {
        try {
            Specializations specializations = specializationRepository.getById(clinicServices.getSpecializationsId());
            return serviceRepository.save(servicesMapper.createClinicServices(clinicServices, specializations));
        } catch (Exception e) {
            throw new ClinicServicesException("Failed to save clinic service", e);
        }
    }

    @Override
    public ClinicServices update(ClinicServices clinicServices) {
        try {
            ClinicServices old = this.getById(clinicServices.getServiceId());
            return serviceRepository.save(servicesMapper.updateClinicServices(old, clinicServices));
        } catch (Exception e) {
            throw new ClinicServicesException("Failed to update clinic service", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            serviceRepository.deleteById(id);
        } catch (Exception e) {
            throw new ClinicServicesException("Failed to delete clinic service", e);
        }
    }

    @Override
    public List<ClinicServices> findAllBySpecializationsId(Long id) {
        try {
            return serviceRepository.findAllBySpecializations_SpecializationId(id);
        } catch (Exception e) {
            throw new ClinicServicesException("Failed to find clinic services by specialization ID", e);
        }
    }


    @Override
    public List<ServiceForClinicsDto> getClinicServiceForClinic() {
        try {
            return serviceRepository.findAll().stream().map(service -> new ServiceForClinicsDto(service.getServiceId(), service.getServiceName())).toList();
        } catch (Exception e) {
            throw new ClinicServicesException("Error getting clinic services for clinic", e);
        }
    }

}
