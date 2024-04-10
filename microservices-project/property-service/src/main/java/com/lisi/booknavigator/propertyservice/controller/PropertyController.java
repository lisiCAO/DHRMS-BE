package com.lisi.booknavigator.propertyservice.controller;

import com.lisi.booknavigator.propertyservice.dto.PropertyRequest;
import com.lisi.booknavigator.propertyservice.dto.PropertyResponse;
import com.lisi.booknavigator.propertyservice.service.PropertyService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProperty(@RequestBody PropertyRequest propertyRequest){
        propertyService.createProperty(propertyRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PropertyResponse> getAllProperties(){
        return propertyService.getAllProperties();
    }
}
