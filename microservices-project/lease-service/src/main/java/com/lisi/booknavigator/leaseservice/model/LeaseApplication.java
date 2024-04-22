package com.lisi.booknavigator.leaseservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


@Entity
@Table(name = "lease_application")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class LeaseApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Property Id must not be empty")
    private String propertyId;

    @NotNull(message = "TenantId must not be empty")
    private Long tenantId;

    @NotBlank(message = "TenantName must not be empty or null")
    private String tenantName;

    @NotBlank(message = "TenantEmail must not be empty or null")
    private String tenantEmail;

    @NotNull(message = "TenantBirthday must not be empty")
    private LocalDateTime tenantBirthday;

    @NotNull(message = "LeaseStartDate must not be empty")
    private LocalDateTime leaseStartDate;

    @NotNull(message = "LeaseEndDate must not be empty")
    private LocalDateTime leaseEndDate;

    @NotNull(message = "NumOfOccupants must not be empty")
    private int numOfOccupants;

    @NotBlank(message = "ApplicationStatus must not be empty or null")
    private String applicationStatus;

}
