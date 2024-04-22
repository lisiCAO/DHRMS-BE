package com.lisi.booknavigator.propertyservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlyAddressRequest {
    @NotBlank(message = "Address must not be empty")
    private String address;
}