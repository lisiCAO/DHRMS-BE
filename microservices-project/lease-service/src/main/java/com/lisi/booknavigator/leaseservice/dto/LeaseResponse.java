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
public class LeaseResponse {
        private Long id;
        private String propertyId;
        private Long tenantUserId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Float monthlyRent;
        private Float deposit;
        private String leaseStatus;
        private String terms;
}
