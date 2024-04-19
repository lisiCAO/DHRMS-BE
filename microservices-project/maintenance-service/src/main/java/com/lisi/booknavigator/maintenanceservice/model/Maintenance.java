package com.lisi.booknavigator.maintenanceservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(value = "maintenancerequest")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Maintenance {
    @Id
    private Integer requestId;
    private Integer propertyId;
    private Integer requestedByUserId;
    private String description;
    private String requestDate;
    private String status;

}


