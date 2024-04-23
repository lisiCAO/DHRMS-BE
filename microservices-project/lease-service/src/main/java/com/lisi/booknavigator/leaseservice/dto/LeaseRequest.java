package com.lisi.booknavigator.leaseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaseRequest {

    @NotBlank(message = "Property Id must not be empty")
    private String propertyId;

    @NotNull(message = "TenantUserId must not be empty")
    private Long tenantUserId;

    @NotNull(message = "StartDate must not be empty")
    private LocalDateTime startDate;

    @NotNull(message = "EndDate must not be empty")
    private LocalDateTime endDate;

    @NotNull(message = "monthlyRent must not be empty")
    private Float monthlyRent;

    @NotNull(message = "Deposit must not be empty")
    private Float deposit;

    @NotBlank(message = "LeaseStatus Id must not be empty")
    private String leaseStatus;

    private String terms;
}
