package com.lisi.booknavigator.leaseservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;


@Document(value = "lease")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Lease {
    @Id
    private Long id;
    private Long propertyId;
    private Long tenantUserId;
    private Date startDate;
    private Date endDate;
    private Float monthlyRent;
    private Float deposit;
    private String leaseStatus;
    private String terms;

}


