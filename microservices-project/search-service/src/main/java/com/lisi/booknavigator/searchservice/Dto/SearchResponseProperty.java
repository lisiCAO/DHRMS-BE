package com.lisi.booknavigator.searchservice.Dto;

import com.lisi.booknavigator.searchservice.entity.Property;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponseProperty {
    private String PropertyId;
    private Property elasticProperty;
}
