package com.lisi.booknavigator.leaseservice.controller;

import com.lisi.booknavigator.leaseservice.dto.LeaseRequest;
import com.lisi.booknavigator.leaseservice.dto.LeaseResponse;
import com.lisi.booknavigator.leaseservice.dto.SearchCondition;
import com.lisi.booknavigator.leaseservice.service.LeaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/leases")
@RequiredArgsConstructor
@Slf4j
public class LeaseController {

    private final LeaseService leaseService;

    @PostMapping
    public ResponseEntity<Object> createLease(@RequestBody @Valid LeaseRequest leaseRequest) {
        try {
            LeaseResponse lease = leaseService.createLease(leaseRequest);
            log.info("Lease created successfully.");

            return ResponseEntity.status(HttpStatus.CREATED).body(lease);
        } catch (Exception e) {
            log.error("Error creating Lease: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Lease: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllLease() {
        try {
            List<LeaseResponse> leases = leaseService.getAllLease();
            if (leases.isEmpty()) {
                log.info("No Lease found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Lease found.");
            } else {
                log.info("Retrieved {} Leases", leases.size());
                return ResponseEntity.ok(leases);
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Lease: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getAllLeaseBySearchCondition(@RequestBody @Valid SearchCondition searchCondition) {
        try {
            List<LeaseResponse> leases = leaseService.getAllLeaseBySearchCondition(searchCondition.getSearchCondition());
            if (leases.isEmpty()) {
                log.info("No Lease found Search Condition = {}.", searchCondition.getSearchCondition());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Lease found, Search Condition =" + searchCondition.getSearchCondition());
            } else {
                log.info("Retrieved {} Leases", leases.size());
                return ResponseEntity.ok(leases);
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrievingLease: " + e.getMessage());
        }
    }

    @GetMapping("/{leaseId}")
    public ResponseEntity<Object> getLeaseById(@PathVariable Long leaseId) {
        try {
            LeaseResponse lease = leaseService.getLeaseById(leaseId);
            if (lease != null) {
                log.info("Lease Id {} retrieved successfully.", leaseId);
                return ResponseEntity.ok(lease);
            } else {
                log.info("Lease ID {} not found.", leaseId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lease not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Lease: " + e.getMessage());
        }
    }

    @PutMapping("/{leaseId}")
    public ResponseEntity<Object> updateLeaseById(@PathVariable Long leaseId, @RequestBody @Valid LeaseRequest leaseRequest) {
        try {
            LeaseResponse updatedLease = leaseService.updateLeaseById(leaseId, leaseRequest);
            if (updatedLease != null) {
                log.info("Lease Id {} updated successfully.", leaseId);
                return ResponseEntity.ok(updatedLease);
            } else {
                log.info("Lease ID {} not found.", leaseId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lease not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Lease: " + e.getMessage());
        }
    }

    @DeleteMapping("/{leaseId}")
    public ResponseEntity<Object> deleteLease(@PathVariable Long leaseId) {
        try{
            boolean isDeleted = leaseService.deleteLeaseById(leaseId);
            if (isDeleted) {
                log.info("Lease Id {} deleted successfully.", leaseId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                log.info("Lease ID {} not found.", leaseId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lease not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving Lease: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Lease: " + e.getMessage());

        }
    }
}
