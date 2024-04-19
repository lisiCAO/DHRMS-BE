package com.lisi.booknavigator.maintenanceservice.controller;

import com.lisi.booknavigator.maintenanceservice.dto.MaintenanceRequest;
import com.lisi.booknavigator.maintenanceservice.dto.MaintenanceResponse;
import com.lisi.booknavigator.maintenanceservice.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-requests")
@RequiredArgsConstructor
@Slf4j
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<Object> createMaintenance(@RequestBody MaintenanceRequest maintenanceRequest) {
        try {
            maintenanceService.createMaintenance(maintenanceRequest);
            log.info("Maintenance created successfully.");

            return ResponseEntity.status(HttpStatus.CREATED).body("Maintenance created successfully.");
        } catch (Exception e) {
            log.error("Error creating maintenance: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating maintenance: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllMaintenances() {
        try {
            List<MaintenanceResponse> maintenances = maintenanceService.getAllMaintenances();
            if (maintenances.isEmpty()) {
                log.info("No maintenances found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No maintenances found.");
            } else {
                log.info("Retrieved {} maintenances", maintenances.size());
                return ResponseEntity.ok(maintenances);
            }
        } catch (Exception e) {
            log.error("Error retrieving maintenances: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving maintenances: " + e.getMessage());
        }
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getMaintenanceById(@PathVariable Integer requestId) {
        try {
            MaintenanceResponse maintenance = maintenanceService.getMaintenanceById(requestId);
            if (maintenance != null) {
                log.info("maintenance Id {} retrieved successfully.", requestId);
                return ResponseEntity.ok(maintenance);
            } else {
                log.info("maintenance ID {} not found.", requestId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("maintenance not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving maintenance: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving maintenance: " + e.getMessage());
        }
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<Object> updateMaintenanceById(@PathVariable Integer requestId, @RequestBody MaintenanceRequest maintenanceRequest) {
        try {
            MaintenanceResponse updatedMaintenance = maintenanceService.updateMaintenanceById(requestId, maintenanceRequest);
            if (updatedMaintenance != null) {
                log.info("maintenance Id {} updated successfully.", requestId);
                return ResponseEntity.ok("maintenance updated successfully.");
            } else {
                log.info("maintenance ID {} not found.", requestId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("maintenance not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving maintenance: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating maintenance: " + e.getMessage());
        }
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Object> deleteMaintenance(@PathVariable Integer requestId) {
        try{
            boolean isDeleted = maintenanceService.deleteMaintenanceById(requestId);
            if (isDeleted) {
                log.info("maintenance Id {} deleted successfully.", requestId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                log.info("maintenance ID {} not found.", requestId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("maintenance not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving maintenance: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting maintenance: " + e.getMessage());

        }
    }
}
