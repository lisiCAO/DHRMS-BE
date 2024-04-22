package com.lisi.booknavigator.leaseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaseApplicationResponse {
    private Long id;
    private String propertyId;
    private Long tenantId;
    private String tenantName;
    private String tenantEmail;
    private LocalDateTime tenantBirthday;
    private LocalDateTime leaseStartDate;
    private LocalDateTime leaseEndDate;
    private int numOfOccupants;
    private String applicationStatus;
}
