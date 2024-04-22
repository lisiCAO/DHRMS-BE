package com.lisi.booknavigator.propertyservice.service;

import com.lisi.booknavigator.propertyservice.dto.OnlyAddressRequest;
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
import com.lisi.booknavigator.propertyservice.model.PropertyType;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final KafkaTemplate<String, PropertyEvent> kafkaTemplate;

    public PropertyResponse createProperty(PropertyRequest propertyRequest){

        // create a default amenities object
        Amenities defaultAmenities = new Amenities(false, false, false, 0, 0, 0.0f);

        Property property = Property.builder()
                .ownerUserId(propertyRequest.getOwnerUserId())
                .address(propertyRequest.getAddress())
                .postcode(propertyRequest.getPostcode())
                .propertytype(Optional.ofNullable(propertyRequest.getPropertytype()).orElse(PropertyType.APARTMENT))
                .propertydescription(Optional.ofNullable(propertyRequest.getPropertydescription()).orElse("Default Description"))
                .amenities(new Amenities(
                        Optional.ofNullable(propertyRequest.getAmenities().getParking()).orElse(defaultAmenities.getParking()),
                        Optional.ofNullable(propertyRequest.getAmenities().getKitchen()).orElse(defaultAmenities.getKitchen()),
                        Optional.ofNullable(propertyRequest.getAmenities().getPool()).orElse(defaultAmenities.getPool()),
                        Optional.ofNullable(propertyRequest.getAmenities().getBedrooms()).orElse(defaultAmenities.getBedrooms()),
                        Optional.ofNullable(propertyRequest.getAmenities().getBathrooms()).orElse(defaultAmenities.getBathrooms()),
                        Optional.ofNullable(propertyRequest.getAmenities().getLivingArea()).orElse(defaultAmenities.getLivingArea())
                ))
                .status(Optional.ofNullable(propertyRequest.getStatus()).orElse("Not Available"))
                .build();

        Property savedProperty = propertyRepository.save(property);

        PropertyEvent event = new PropertyEvent(savedProperty.getId(), PropertyEvent.EventType.CREATE, savedProperty);
        kafkaTemplate.send("propertiesTopic",event);

        log.info("Property {} is saved", property.getId());

        return mapToPropertyResponse(property);
    }

    public PropertyResponse createOnlyAddressProperty(OnlyAddressRequest onlyAddressRequest){

        // create a default amenities object
        Amenities defaultAmenities = new Amenities(false, false, false, 0, 0, 0.0f);

        Property property = Property.builder()
                .ownerUserId(0L)
                .address(onlyAddressRequest.getAddress())
                .postcode("A1A 1A1")
                .propertytype(PropertyType.APARTMENT)
                .propertydescription("Default Description")
                .amenities(defaultAmenities)
                .status("Not Available")
                .build();

        Property savedProperty = propertyRepository.save(property);

        PropertyEvent event = new PropertyEvent(savedProperty.getId(), PropertyEvent.EventType.CREATE, savedProperty);
        kafkaTemplate.send("propertiesTopic",event);

        log.info("Address Only Property {} is saved", savedProperty.getId());

        return mapToPropertyResponse(savedProperty);
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

            // update the property fields if they are not null
            if (propertyRequest.getOwnerUserId() != null) {
                property.setOwnerUserId(propertyRequest.getOwnerUserId());
            }
            if (propertyRequest.getAddress() != null) {
                property.setAddress(propertyRequest.getAddress());
            }
            if (propertyRequest.getPostcode() != null) {
                property.setPostcode(propertyRequest.getPostcode());
            }
            if (propertyRequest.getPropertytype() != null) {
                property.setPropertytype(propertyRequest.getPropertytype());
            }
            if (propertyRequest.getPropertydescription() != null) {
                property.setPropertydescription(propertyRequest.getPropertydescription());
            }
            if (propertyRequest.getAmenities() != null) {
                Amenities reqAmenities = propertyRequest.getAmenities();
                Amenities currentAmenities = property.getAmenities();
                if (reqAmenities.getParking() != null) {
                    currentAmenities.setParking(reqAmenities.getParking());
                }
                if (reqAmenities.getKitchen() != null) {
                    currentAmenities.setKitchen(reqAmenities.getKitchen());
                }
                if (reqAmenities.getPool() != null) {
                    currentAmenities.setPool(reqAmenities.getPool());
                }
                if (reqAmenities.getBedrooms() != null) {
                    currentAmenities.setBedrooms(reqAmenities.getBedrooms());
                }
                if (reqAmenities.getBathrooms() != null) {
                    currentAmenities.setBathrooms(reqAmenities.getBathrooms());
                }
                if (reqAmenities.getLivingArea() != null) {
                    currentAmenities.setLivingArea(reqAmenities.getLivingArea());
                }
            }
            if (propertyRequest.getStatus() != null) {
                property.setStatus(propertyRequest.getStatus());
            }

            // save the updated property
            Property updatedProperty = propertyRepository.save(property);

            PropertyEvent event = new PropertyEvent(updatedProperty.getId(), PropertyEvent.EventType.UPDATE, updatedProperty);
            kafkaTemplate.send("propertiesTopic",event);

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

            PropertyEvent event = new PropertyEvent(propertyId, PropertyEvent.EventType.DELETE, null);
            kafkaTemplate.send("propertiesTopic",event);

            log.info("Property ID {} deleted", propertyId);
            return true; // delete operation executed successfully
        } else {
            log.info("Property ID {} not found.", propertyId);

            return false; // property not found
        }
    }
}