package com.lisi.booknavigator.propertyservice.controller;

import com.lisi.booknavigator.propertyservice.dto.PropertyRequest;
import com.lisi.booknavigator.propertyservice.dto.PropertyResponse;
import com.lisi.booknavigator.propertyservice.service.PropertyService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<Object> createProperty(@RequestBody PropertyRequest propertyRequest) {
        try {
            propertyService.createProperty(propertyRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Property created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating property: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllProperties() {
        try {
            List<PropertyResponse> properties = propertyService.getAllProperties();
            if (properties.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No properties found.");
            } else {
                return ResponseEntity.ok(properties);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving properties: " + e.getMessage());
        }
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<Object> getPropertyById(@PathVariable String propertyId) {
        try {
            PropertyResponse property = propertyService.getPropertyById(propertyId);
            if (property != null) {
                return ResponseEntity.ok(property);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving property: " + e.getMessage());
        }
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<Object> updatePropertyById(@PathVariable String propertyId, @RequestBody PropertyRequest propertyRequest) {
        try {
            PropertyResponse updatedProperty = propertyService.updatePropertyById(propertyId, propertyRequest);
            if (updatedProperty != null) {
                return ResponseEntity.ok("Property updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating property: " + e.getMessage());
        }
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Object> deleteProperty(@PathVariable String propertyId) {
        try{
            boolean isDeleted = propertyService.deletePropertyById(propertyId);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting property: " + e.getMessage());

        }
    }
}
