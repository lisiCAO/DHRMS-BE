package com.lisi.booknavigator.propertyservice.service;

import com.lisi.booknavigator.propertyservice.dto.PropertyRequest;
import com.lisi.booknavigator.propertyservice.dto.PropertyResponse;
import com.lisi.booknavigator.propertyservice.event.PropertyEvent;
import com.lisi.booknavigator.propertyservice.model.Amenities;
import com.lisi.booknavigator.propertyservice.model.Property;
import com.lisi.booknavigator.propertyservice.repository.PropertyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final KafkaTemplate<String, PropertyEvent> kafkaTemplate;

    public void createProperty(PropertyRequest propertyRequest){

        Property property = Property.builder()
                .ownerUserId(propertyRequest.getOwnerUserId())
                .address(propertyRequest.getAddress())
                .postcode(propertyRequest.getPostcode())
                .propertytype(propertyRequest.getPropertytype())
                .propertydescription(propertyRequest.getPropertydescription())
                .amenities(new Amenities(
                        propertyRequest.getAmenities().getParking(),
                        propertyRequest.getAmenities().getKitchen(),
                        propertyRequest.getAmenities().getPool(),
                        propertyRequest.getAmenities().getBedrooms(),
                        propertyRequest.getAmenities().getBathrooms(),
                        propertyRequest.getAmenities().getLivingArea()
                ))
                .status(propertyRequest.getStatus())
                .build();

        Property savedProperty = propertyRepository.save(property);

        PropertyEvent event = new PropertyEvent(savedProperty.getId(), PropertyEvent.EventType.CREATE, savedProperty);
        kafkaTemplate.send("propertiesTopic",event);

        log.info("Property {} is saved", property.getId());
    }

    public List<PropertyResponse> getAllProperties(){
        List<Property> properties = propertyRepository.findAll();

        return properties.stream().map(this::mapToPropertyResponse).toList();
    }

    private PropertyResponse mapToPropertyResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .ownerUserId(property.getOwnerUserId())
                .address(property.getAddress())
                .postcode(property.getPostcode())
                .propertytype(property.getPropertytype())
                .propertydescription(property.getPropertydescription())
                .amenities(property.getAmenities())
                .status(property.getStatus())
                .build();
    }
}