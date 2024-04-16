package com.lisi.booknavigator.searchservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "properties")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Property {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private Long ownerUserId;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Keyword)
    private String postcode;

    @Field(type = FieldType.Keyword)
    private String propertyType;

    @Field(type = FieldType.Text)
    private String propertyDescription;

    // Assume amenities is embedded and should be indexed appropriately
    @Field(type = FieldType.Nested)
    private Amenities amenities;

    @Field(type = FieldType.Keyword)
    private String status;
}
