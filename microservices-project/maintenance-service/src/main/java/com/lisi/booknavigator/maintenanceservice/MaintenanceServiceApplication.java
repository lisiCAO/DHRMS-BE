package com.lisi.booknavigator.maintenanceservice;

import com.lisi.booknavigator.maintenanceservice.dto.MaintenanceRequest;
import com.lisi.booknavigator.maintenanceservice.model.MaintenanceStatus;
import com.lisi.booknavigator.maintenanceservice.service.MaintenanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class MaintenanceServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(MaintenanceServiceApplication.class, args);
        }

    @Bean
    CommandLineRunner run(MaintenanceService maintenanceService) {
        return args -> {
            if (maintenanceService.getAllMaintenances().isEmpty()) {
                // Creating mock Maintenance 1
                log.info("No Maintenances found in the database. Creating mock Maintenance Request.");
                MaintenanceRequest maintenanceRequest1 = new MaintenanceRequest(
                        4,
                        24,
                        "Bulb change",
                        "April 20, 2024",
                        "Open"
                );



                // Saving mock properties to the database
                maintenanceService.createMaintenance(maintenanceRequest1);
                //maintenanceService.createMaintenance(maintenanceRequest2);
                log.info("Mock Maintenance created successfully.");
            }
        };
    }
}