package com.lisi.booknavigator.leaseservice.dto;

import com.lisi.booknavigator.leaseservice.model.LeaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaseRequest {
    private Long propertyId;
    private Long tenantUserId;
    private Date startDate;
    private Date endDate;
    private Float monthlyRent;
    private Float deposit;
    private String leaseStatus;
    private String terms;
}
