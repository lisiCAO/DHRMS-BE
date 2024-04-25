package com.lisi.booknavigator.leaseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Amenities {
    private Boolean parking;
    private Boolean kitchen;
    private Boolean pool;
    private Integer bedrooms;
    private Integer bathrooms;
    private Float livingArea;
}
