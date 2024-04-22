package com.lisi.booknavigator.propertyservice.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PropertyTypeSubsetValidator implements ConstraintValidator<PropertyTypeSubset, PropertyType> {
    private PropertyType[] subset;

    @Override
    public void initialize(PropertyTypeSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(PropertyType value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}

