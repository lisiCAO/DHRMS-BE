package com.lisi.booknavigator.leaseservice.service;

import com.lisi.booknavigator.leaseservice.dto.PropertyResponse;
import com.lisi.booknavigator.leaseservice.dto.LeaseRequest;
import com.lisi.booknavigator.leaseservice.dto.LeaseResponse;
//import com.lisi.booknavigator.leaseservice.event.LeaseEvent;
import com.lisi.booknavigator.leaseservice.model.Lease;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.lisi.booknavigator.leaseservice.repository.LeaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LeaseService {

    private final LeaseRepository leaseRepository;
    //private final KafkaTemplate<String, LeaseEvent> kafkaTemplate;
    private final RestTemplate restTemplate;

    private final String propertyServiceUrl;

    // 使用构造函数注入URL
    public LeaseService(LeaseRepository leaseRepository,
                        RestTemplate restTemplate,
                        @Value("${property.service.url}") String propertyServiceUrl) {
        this.leaseRepository = leaseRepository;
        this.restTemplate = restTemplate;
        this.propertyServiceUrl = propertyServiceUrl;
    }

    public LeaseResponse createLease(LeaseRequest leaseRequest){


        // Call the property service to fetch the property details
        ResponseEntity<PropertyResponse> response = restTemplate.getForEntity(propertyServiceUrl, PropertyResponse.class, leaseRequest.getPropertyId());

        if (response.getStatusCode() == HttpStatus.OK) {
            //PropertyResponse property = response.getBody();
            // Assume property must be not null to continue
            log.info("Property id: {} retrieved successfully", leaseRequest.getPropertyId());

            Lease lease = Lease.builder()
                    .propertyId(leaseRequest.getPropertyId())
                    .tenantUserId(leaseRequest.getTenantUserId())
                    .startDate(leaseRequest.getStartDate())
                    .endDate(leaseRequest.getEndDate())
                    .monthlyRent(leaseRequest.getMonthlyRent())
                    .deposit(leaseRequest.getDeposit())
                    .leaseStatus(leaseRequest.getLeaseStatus())
                    .terms(leaseRequest.getTerms())
                    .build();

            Lease savedLease = leaseRepository.save(lease);

            //LeaseEvent event = new LeaseEvent(savedLease.getId(), LeaseEvent.EventType.CREATE, savedLease);
            //kafkaTemplate.send("leasesTopic",event);

            log.info("lease {} is saved", savedLease.getId());

            return mapToLeaseResponse(savedLease);

        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.error("Property id {} not found", leaseRequest.getPropertyId());
            throw new RuntimeException("Property id " + leaseRequest.getPropertyId() +" not found");
        } else {
            log.error("Error call Property Service API");
            throw new RuntimeException("Error call Property Service API");
        }
    }

    public List<LeaseResponse> getAllLease(){
        List<Lease> leases = leaseRepository.findAll();

        log.info("Retrieved {} leases", leases.size());

        return leases.stream().map(this::mapToLeaseResponse).toList();
    }

    public List<LeaseResponse> getAllLeaseBySearchCondition(String searchCondition){

        // Normalize the input to handle case insensitivity
        String normalizedSearchCondition = searchCondition.trim().toLowerCase();

        // Check if the normalized search condition is one of the valid statuses
        if (normalizedSearchCondition.equals("pending") || normalizedSearchCondition.equals("approved") || normalizedSearchCondition.equals("denied")) {
            List<Lease> leases = leaseRepository.findByLeaseStatus(searchCondition);
            log.info("Retrieved {} Lease(s)", leases.size());

            return leases.stream()
                    .map(this::mapToLeaseResponse)
                    .toList();
        } else {
            log.warn("Invalid search condition: {}", searchCondition);
            return List.of();  // Return an empty list if the condition is not valid
        }
    }

    private LeaseResponse mapToLeaseResponse(Lease lease) {
        return LeaseResponse.builder()
                .id(lease.getId())
                .propertyId(lease.getPropertyId())
                .tenantUserId(lease.getTenantUserId())
                .startDate(lease.getStartDate())
                .endDate(lease.getEndDate())
                .monthlyRent(lease.getMonthlyRent())
                .deposit(lease.getDeposit())
                .leaseStatus(lease.getLeaseStatus())
                .terms(lease.getTerms())
                .build();
    }

    public LeaseResponse getLeaseById(Long leaseId) {
        Optional<Lease> leaseOpt = leaseRepository.findById(leaseId);
        if (leaseOpt.isPresent()) {
            Lease lease = leaseOpt.get();
            log.info("lease {} retrieved", lease);
            return mapToLeaseResponse(lease);
        } else {
            log.info("lease Id {} not found.", leaseId);
            return null;
        }
    }

    public LeaseResponse updateLeaseById(Long leaseId, LeaseRequest leaseRequest) {
        Optional<Lease> leaseOpt = leaseRepository.findById(leaseId);
        if (leaseOpt.isPresent()) {
            Lease lease = leaseOpt.get();

            //update lease fields

            lease.setPropertyId(leaseRequest.getPropertyId());
            lease.setTenantUserId(leaseRequest.getTenantUserId());
            lease.setStartDate(leaseRequest.getStartDate());
            lease.setEndDate(leaseRequest.getEndDate());
            lease.setMonthlyRent(leaseRequest.getMonthlyRent());
            lease.setDeposit(leaseRequest.getDeposit());
            lease.setLeaseStatus(leaseRequest.getLeaseStatus());
            lease.setTerms(leaseRequest.getTerms());

            // save the updated lease
            Lease updatedLease = leaseRepository.save(lease);

            //LeaseEvent event = new LeaseEvent(updatedLease.getId(), LeaseEvent.EventType.UPDATE, updatedLease);
            //kafkaTemplate.send("leasesTopic",event);

            // convert the updated lease to response object
            log.info("lease {} updated", updatedLease);
            return mapToLeaseResponse(updatedLease);
        } else {
            log.info("lease Id {} not found.", leaseId);
            return null;
        }
    }

    public boolean deleteLeaseById(Long leaseId) {
        if (leaseRepository.existsById(leaseId)) {
            leaseRepository.deleteById(leaseId);

            //LeaseEvent event = new LeaseEvent(leaseId, LeaseEvent.EventType.DELETE, null);
            //kafkaTemplate.send("leasesTopic",event);

            log.info("lease Id {} deleted", leaseId);
            return true; // delete operation executed successfully
        } else {
            log.info("lease Id {} not found.", leaseId);

            return false; // lease not found
        }
    }
}