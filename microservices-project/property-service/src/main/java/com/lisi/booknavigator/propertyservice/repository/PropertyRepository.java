package com.lisi.booknavigator.propertyservice.repository;

import com.lisi.booknavigator.propertyservice.model.Property;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PropertyRepository extends MongoRepository<Property, String> {
}
