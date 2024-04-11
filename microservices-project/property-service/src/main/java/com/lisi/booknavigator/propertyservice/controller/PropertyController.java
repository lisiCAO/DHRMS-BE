package com.lisi.booknavigator.propertyservice.controller;

import com.lisi.booknavigator.propertyservice.dto.PropertyRequest;
import com.lisi.booknavigator.propertyservice.dto.PropertyResponse;
import com.lisi.booknavigator.propertyservice.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<Void>> createProperty(@RequestBody PropertyRequest propertyRequest) {
        try {
            propertyService.createProperty(propertyRequest);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Property created successfully.", null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<Void> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creating property: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getAllProperties() {
        try {
            List<PropertyResponse> properties = propertyService.getAllProperties();
            ApiResponse<List<PropertyResponse>> response = new ApiResponse<>(HttpStatus.OK.value(), "Properties retrieved successfully.", properties);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<PropertyResponse>> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error retrieving properties: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
