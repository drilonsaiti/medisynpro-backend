package com.example.medisyncpro.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "SERVICE")
@NoArgsConstructor
public class ClinicServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specializations specializations;

    public ClinicServices(String serviceName, Integer durationMinutes, BigDecimal price, Specializations specializations) {
        this.serviceName = serviceName;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.specializations = specializations;
    }
}