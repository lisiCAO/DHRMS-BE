package com.lisi.booknavigator.propertyservice.dto;

import com.lisi.booknavigator.propertyservice.model.PropertyType;
import com.lisi.booknavigator.propertyservice.model.Amenities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequest {
    private Long ownerUserId;
    private String address;
    private String postcode;
    private PropertyType propertytype;
    private String propertydescription;
    private Amenities amenities;
    private String status;
}
