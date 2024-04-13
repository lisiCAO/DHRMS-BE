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
import java.util.Optional;

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

        log.info("Retrieved {} properties", properties.size());

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

    public PropertyResponse getPropertyById(String propertyId) {
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            log.info("Property {} retrieved", property);
            return mapToPropertyResponse(property);
        } else {
            log.info("Property ID {} not found.", propertyId);
            return null;
        }
    }

    public PropertyResponse updatePropertyById(String propertyId, PropertyRequest propertyRequest) {
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();

            //update property fields
            property.setOwnerUserId(propertyRequest.getOwnerUserId());
            property.setAddress(propertyRequest.getAddress());
            property.setPostcode(propertyRequest.getPostcode());
            property.setPropertytype(propertyRequest.getPropertytype());
            property.setPropertydescription(propertyRequest.getPropertydescription());
            property.setAmenities(new Amenities(
                    propertyRequest.getAmenities().getParking(),
                    propertyRequest.getAmenities().getKitchen(),
                    propertyRequest.getAmenities().getPool(),
                    propertyRequest.getAmenities().getBedrooms(),
                    propertyRequest.getAmenities().getBathrooms(),
                    propertyRequest.getAmenities().getLivingArea()));
            property.setStatus(propertyRequest.getStatus());

            // save the updated property
            Property updatedProperty = propertyRepository.save(property);

            // convert the updated property to response object
            log.info("Property {} updated", property);
            return mapToPropertyResponse(updatedProperty);
        } else {
            log.info("Property ID {} not found.", propertyId);
            return null;
        }
    }

    public boolean deletePropertyById(String propertyId) {
        if (propertyRepository.existsById(propertyId)) {
            propertyRepository.deleteById(propertyId);
            log.info("Property ID {} deleted", propertyId);
            return true; // delete operation executed successfully
        } else {
            log.info("Property ID {} not found.", propertyId);

            return false; // property not found
        }
    }
}