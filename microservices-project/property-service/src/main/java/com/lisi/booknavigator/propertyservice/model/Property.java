package com.lisi.booknavigator.propertyservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(value = "property")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Property {
    @Id
    private String id;
    private Long ownerUserId;
    private String address;
    private String postcode;
    private PropertyType propertytype;
    private String propertydescription;
    private Amenities amenities;
    private String status;
}


