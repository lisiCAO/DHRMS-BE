package com.lisi.booknavigator.propertyservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

@Document(value = "property")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Property {
    @Id
    private String id;
    @NotNull(message = "Owner User Id must not be null")
    private Long ownerUserId;
    @NotBlank(message = "Address must not be empty")
    private String address;
    @NotBlank(message = "Postcode must not be empty")
    @Pattern(regexp = "^[A-Z]\\d[A-Z] ?\\d[A-Z]\\d$", message = "Invalid postcode, should be in the format of A1A 1A1 or A1A1A1")
    private String postcode;
    @PropertyTypeSubset(anyOf = {PropertyType.APARTMENT, PropertyType.HOUSE, PropertyType.CONDO, PropertyType.TOWNHOUSE})
    private PropertyType propertytype;
    private String propertydescription;
    private Amenities amenities;
    private String status;
}


