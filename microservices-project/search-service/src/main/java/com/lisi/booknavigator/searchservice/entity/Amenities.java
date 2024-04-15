package com.lisi.booknavigator.searchservice.entity;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Amenities {
    @Field(type = FieldType.Boolean)
    private Boolean parking;

    @Field(type = FieldType.Boolean)
    private Boolean kitchen;

    @Field(type = FieldType.Boolean)
    private Boolean pool;

    @Field(type = FieldType.Integer)
    private Integer bedrooms;

    @Field(type = FieldType.Integer)
    private Integer bathrooms;

    @Field(type = FieldType.Float)
    private Float livingArea;
}

