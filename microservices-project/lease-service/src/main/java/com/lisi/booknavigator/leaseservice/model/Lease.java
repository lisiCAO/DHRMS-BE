package com.lisi.booknavigator.leaseservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
@Entity
@Table(name = "leases")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Lease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "property_id")
    private String propertyId;

    @Column(name = "tenant_user_id")
    private Long tenantUserId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "monthly_rent")
    private Float monthlyRent;

    @Column(name = "deposit")
    private Float deposit;

    @Column(name = "lease_status")
    private String leaseStatus;

    @Column(name = "terms")
    private String terms;

}


