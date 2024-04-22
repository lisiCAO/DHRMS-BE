package com.lisi.booknavigator.propertyservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.lisi.booknavigator.propertyservice.dto.PropertyRequest;
import com.lisi.booknavigator.propertyservice.service.PropertyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import com.lisi.booknavigator.propertyservice.model.Amenities;
import com.lisi.booknavigator.propertyservice.model.PropertyType;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class PropertyServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(PropertyServiceApplication.class, args);
        }

    @Bean
    CommandLineRunner run(PropertyService propertyService) {
        return args -> {
            if (propertyService.getAllProperties().isEmpty()) {
                // Creating mock property 1
                log.info("No properties found in the database. Creating mock properties.");
                PropertyRequest propertyRequest1 = new PropertyRequest(
                        Long.valueOf("1"),
                        "123 Main Street, City, Country",
                        "A1B 2C3",
                        PropertyType.APARTMENT,
                        "A beautiful apartment with sea view",
                        new Amenities(true, true, false, 2, 1, Float.valueOf("200.0")),
                        "AVAILABLE"
                );

                // Creating mock property 2
                PropertyRequest propertyRequest2 = new PropertyRequest(
                        Long.valueOf("2"),
                        "456 Elm Street, City, Country",
                        "C1A2Z3",
                        PropertyType.HOUSE,
                        "A cozy house with a big backyard",
                        new Amenities(false, true, true, 3, 2, Float.valueOf("200.0")),
                        "LEASED"
                );

                // Saving mock properties to the database
                propertyService.createProperty(propertyRequest1);
                propertyService.createProperty(propertyRequest2);
                log.info("Mock properties created successfully.");
            }
        };
    }
}