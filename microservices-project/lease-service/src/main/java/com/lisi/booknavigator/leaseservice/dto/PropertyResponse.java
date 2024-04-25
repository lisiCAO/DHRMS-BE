package com.lisi.booknavigator.leaseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
    private String id;
    private Long ownerUserId;
    private String address;
    private String postcode;
    private PropertyType propertytype;
    private String propertydescription;
    private Amenities amenities;
    private String status;
}
