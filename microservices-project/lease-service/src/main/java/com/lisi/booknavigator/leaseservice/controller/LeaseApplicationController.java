package com.lisi.booknavigator.leaseservice.controller;

import com.lisi.booknavigator.leaseservice.dto.LeaseApplicationRequest;
import com.lisi.booknavigator.leaseservice.dto.LeaseApplicationResponse;
import com.lisi.booknavigator.leaseservice.service.LeaseApplicationService;
import com.lisi.booknavigator.leaseservice.dto.SearchCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/leaseapplications")
@RequiredArgsConstructor
@Slf4j
public class LeaseApplicationController {

    private final LeaseApplicationService leaseApplicationService;

    @PostMapping
    public ResponseEntity<Object> createLeaseApplication(@RequestBody @Valid LeaseApplicationRequest leaseApplicationRequest) {
        try {
            LeaseApplicationResponse leaseApplication = leaseApplicationService.createLeaseApplication(leaseApplicationRequest);
            log.info("Lease application created successfully.");

            return ResponseEntity.status(HttpStatus.CREATED).body(leaseApplication);
        } catch (Exception e) {
            log.error("Error creating Lease Application: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Lease Application: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllLeaseApplication() {
        try {
            List<LeaseApplicationResponse> leaseApplications = leaseApplicationService.getAllLeaseApplication();
            if (leaseApplications.isEmpty()) {
                log.info("No Lease Application found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Lease Application found.");
            } else {
                log.info("Retrieved {} Lease applications", leaseApplications.size());
                return ResponseEntity.ok(leaseApplications);
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease Application: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Lease Application: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getAllLeaseApplicationBySearchCondition(@RequestBody @Valid SearchCondition searchCondition) {
        try {
            List<LeaseApplicationResponse> leaseApplications = leaseApplicationService.getAllLeaseApplicationBySearchCondition(searchCondition.getSearchCondition());
            if (leaseApplications.isEmpty()) {
                log.info("No Lease Application found Search Condition = {}.", searchCondition.getSearchCondition());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Lease Application found, Search Condition =" + searchCondition.getSearchCondition());
            } else {
                log.info("Retrieved {} Lease applications", leaseApplications.size());
                return ResponseEntity.ok(leaseApplications);
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease Application: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Lease Application: " + e.getMessage());
        }
    }

    @GetMapping("/{leaseApplicationId}")
    public ResponseEntity<Object> getLeaseApplicationById(@PathVariable Long leaseApplicationId) {
        try {
            LeaseApplicationResponse leaseApplication = leaseApplicationService.getLeaseApplicationById(leaseApplicationId);
            if (leaseApplication != null) {
                log.info("Lease Application Id {} retrieved successfully.", leaseApplicationId);
                return ResponseEntity.ok(leaseApplication);
            } else {
                log.info("Lease Application Id {} not found.", leaseApplicationId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lease Application not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease Application: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Lease Application: " + e.getMessage());
        }
    }

    @PutMapping("/{leaseApplicationId}")
    public ResponseEntity<Object> updateLeaseApplicationById(@PathVariable Long leaseApplicationId, @RequestBody @Valid LeaseApplicationRequest leaseApplicationRequest) {
        try {
            LeaseApplicationResponse updatedLeaseApplication = leaseApplicationService.updateLeaseApplicationById(leaseApplicationId, leaseApplicationRequest);
            if (updatedLeaseApplication != null) {
                log.info("Lease Application Id {} updated successfully.", leaseApplicationId);
                return ResponseEntity.ok(updatedLeaseApplication);
            } else {
                log.info("Lease Application Id {} not found.", leaseApplicationId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lease Application not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease Application: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Lease Application: " + e.getMessage());
        }
    }

    @DeleteMapping("/{leaseApplicationId}")
    public ResponseEntity<Object> deleteLeaseApplication(@PathVariable Long leaseApplicationId) {
        try{
            boolean isDeleted = leaseApplicationService.deleteLeaseApplicationById(leaseApplicationId);
            if (isDeleted) {
                log.info("Lease Application Id {} deleted successfully.", leaseApplicationId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                log.info("Lease Application ID {} not found.", leaseApplicationId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lease Application not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease Application: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Lease Application: " + e.getMessage());

        }
    }
}
