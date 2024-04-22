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
public class LeaseApplicationRequest {

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
