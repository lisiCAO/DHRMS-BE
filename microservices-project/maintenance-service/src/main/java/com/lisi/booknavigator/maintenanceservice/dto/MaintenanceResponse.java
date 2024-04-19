package com.lisi.booknavigator.maintenanceservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceResponse {
        private Integer requestId;
        private Integer propertyId;
        private Integer requestedByUserId;
        private String description;
        private String requestDate;
        private String status;
}
