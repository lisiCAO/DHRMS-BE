package com.lisi.booknavigator.leaseservice.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCondition {

    @NotBlank(message = "Search Condition must not be empty")
    private String searchCondition;

}
