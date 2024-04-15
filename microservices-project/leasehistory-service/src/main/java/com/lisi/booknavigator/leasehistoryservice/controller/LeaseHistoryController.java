package com.lisi.booknavigator.leasehistoryservice.controller;

import com.lisi.booknavigator.leasehistoryservice.dto.LeaseHistoryRequest;
import com.lisi.booknavigator.leasehistoryservice.dto.LeaseHistoryResponse;
import com.lisi.booknavigator.leasehistoryservice.service.LeaseHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaseHistories")
@RequiredArgsConstructor
@Slf4j
public class LeaseHistoryController {

    private final LeaseHistoryService leasehistoryService;

    @PostMapping
    public ResponseEntity<Object> createProperty(@RequestBody LeaseHistoryRequest leasehistoryRequest) {
        try {
            leasehistoryService.createLeaseHistory(leasehistoryRequest);
            log.info("leasehistory created successfully.");

            return ResponseEntity.status(HttpStatus.CREATED).body("leasehistory created successfully.");
        } catch (Exception e) {
            log.error("Error creating leasehistory: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating leasehistory: " + e.getMessage());
        }
    }



    @GetMapping("/{leasehistoryId}")
    public ResponseEntity<Object> getLeaseHistoryById(@PathVariable String historyId) {
        try {
            LeaseHistoryResponse leasehistory = leasehistoryService.getPropertyById(historyId);
            if (leasehistory != null) {
                log.info("leasehistory Id {} retrieved successfully.", historyId);
                return ResponseEntity.ok(leasehistory);
            } else {
                log.info("leasehistory ID {} not found.", historyId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("leasehistory not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving leasehistory: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving leasehistory: " + e.getMessage());
        }
    }


}
