package com.lisi.booknavigator.leaseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaseResponse {
        private Long id;
        private String propertyId;
        private Long tenantUserId;
        private Date startDate;
        private Date endDate;
        private Float monthlyRent;
        private Float deposit;
        private LeaseType leaseStatus;
        private String terms;
}
