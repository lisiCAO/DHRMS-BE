package com.lisi.booknavigator.leaseservice.service;

import com.lisi.booknavigator.leaseservice.dto.LeaseApplicationRequest;
import com.lisi.booknavigator.leaseservice.dto.LeaseApplicationResponse;
import com.lisi.booknavigator.leaseservice.dto.PropertyResponse;
//import com.lisi.booknavigator.leaseservice.event.LeaseApplicationEvent;
import com.lisi.booknavigator.leaseservice.model.LeaseApplication;
import com.lisi.booknavigator.leaseservice.repository.LeaseApplicationRepository;
import com.lisi.booknavigator.leaseservice.repository.LeaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LeaseApplicationService {

    private final LeaseApplicationRepository leaseApplicationRepository;
    //private final KafkaTemplate<String, LeaseApplicationEvent> kafkaTemplate;
    private final RestTemplate restTemplate;

    private final String propertyServiceUrl;

    // 使用构造函数注入URL
    public LeaseApplicationService(LeaseApplicationRepository leaseApplicationRepository,
                        RestTemplate restTemplate,
                        @Value("${property.service.url}") String propertyServiceUrl) {
        this.leaseApplicationRepository = leaseApplicationRepository;
        this.restTemplate = restTemplate;
        this.propertyServiceUrl = propertyServiceUrl;
    }

    public LeaseApplicationResponse createLeaseApplication(LeaseApplicationRequest leaseApplicationRequest){

        // Call the property service to fetch the property details
        ResponseEntity<PropertyResponse> response = restTemplate.getForEntity(propertyServiceUrl, PropertyResponse.class, leaseApplicationRequest.getPropertyId());

        if (response.getStatusCode() == HttpStatus.OK) {
            //PropertyResponse property = response.getBody();
            // Assume property must be not null to continue
            log.info("Property id: {} retrieved successfully", leaseApplicationRequest.getPropertyId());

            LeaseApplication leaseApplication = LeaseApplication.builder()
                    .propertyId(leaseApplicationRequest.getPropertyId())
                    .tenantId(leaseApplicationRequest.getTenantId())
                    .tenantName(leaseApplicationRequest.getTenantName())
                    .tenantEmail(leaseApplicationRequest.getTenantEmail())
                    .tenantBirthday(leaseApplicationRequest.getTenantBirthday())
                    .leaseStartDate(leaseApplicationRequest.getLeaseStartDate())
                    .leaseEndDate(leaseApplicationRequest.getLeaseEndDate())
                    .numOfOccupants(leaseApplicationRequest.getNumOfOccupants())
                    .applicationStatus(leaseApplicationRequest.getApplicationStatus())
                    .build();

            LeaseApplication savedLeaseApplication = leaseApplicationRepository.save(leaseApplication);

            //LeaseApplicationEvent event = new LeaseApplicationEvent(savedLeaseApplication.getId(), LeaseApplicationEvent.EventType.CREATE, savedLeaseApplication);
            //kafkaTemplate.send("leaseApplicationsTopic",event);

            log.info("lease Application {} is saved", savedLeaseApplication.getId());

            return mapToLeaseApplicationResponse(savedLeaseApplication);
        }
        else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.error("Property id {} not found", leaseApplicationRequest.getPropertyId());
            throw new RuntimeException("Property id " + leaseApplicationRequest.getPropertyId() +" not found");
        } else {
            log.error("Error call Property Service API");
            throw new RuntimeException("Error call Property Service API");
        }
    }

    public List<LeaseApplicationResponse> getAllLeaseApplication(){
        List<LeaseApplication> leaseApplications = leaseApplicationRepository.findAll();

        log.info("Retrieved {} LeaseApplication", leaseApplications.size());

        return leaseApplications.stream().map(this::mapToLeaseApplicationResponse).toList();
    }

    public List<LeaseApplicationResponse> getAllLeaseApplicationBySearchCondition(String searchCondition){

        // Normalize the input to handle case insensitivity
        String normalizedSearchCondition = searchCondition.trim().toLowerCase();

        // Check if the normalized search condition is one of the valid statuses
        if (normalizedSearchCondition.equals("pending") || normalizedSearchCondition.equals("approved") || normalizedSearchCondition.equals("denied")) {
            List<LeaseApplication> leaseApplications = leaseApplicationRepository.findByApplicationStatus(searchCondition);
            log.info("Retrieved {} LeaseApplication(s)", leaseApplications.size());

            return leaseApplications.stream()
                    .map(this::mapToLeaseApplicationResponse)
                    .toList();
        } else {
            log.warn("Invalid search condition: {}", searchCondition);
            return List.of();  // Return an empty list if the condition is not valid
        }
    }

    private LeaseApplicationResponse mapToLeaseApplicationResponse(LeaseApplication leaseApplication) {
        return LeaseApplicationResponse.builder()
                .id(leaseApplication.getId())
                .propertyId(leaseApplication.getPropertyId())
                .tenantId(leaseApplication.getTenantId())
                .tenantName(leaseApplication.getTenantName())
                .tenantEmail(leaseApplication.getTenantEmail())
                .tenantBirthday(leaseApplication.getTenantBirthday())
                .leaseStartDate(leaseApplication.getLeaseStartDate())
                .leaseEndDate(leaseApplication.getLeaseEndDate())
                .numOfOccupants(leaseApplication.getNumOfOccupants())
                .applicationStatus(leaseApplication.getApplicationStatus())
                .build();
    }

    public LeaseApplicationResponse getLeaseApplicationById(Long leaseApplicationId) {
        Optional<LeaseApplication> leaseApplicationOpt = leaseApplicationRepository.findById(leaseApplicationId);
        if (leaseApplicationOpt.isPresent()) {
            LeaseApplication leaseApplication = leaseApplicationOpt.get();
            log.info("lease Application {} retrieved", leaseApplication);
            return mapToLeaseApplicationResponse(leaseApplication);
        } else {
            log.info("lease Application Id {} not found.", leaseApplicationId);
            return null;
        }
    }

    public LeaseApplicationResponse updateLeaseApplicationById(Long leaseApplicationId, LeaseApplicationRequest leaseApplicationRequest) {
        Optional<LeaseApplication> leaseApplicationOpt = leaseApplicationRepository.findById(leaseApplicationId);
        if (leaseApplicationOpt.isPresent()) {
            LeaseApplication leaseApplication = leaseApplicationOpt.get();

            //update lease Application fields
            leaseApplication.setPropertyId(leaseApplicationRequest.getPropertyId());
            leaseApplication.setTenantId(leaseApplicationRequest.getTenantId());
            leaseApplication.setTenantName(leaseApplicationRequest.getTenantName());
            leaseApplication.setTenantEmail(leaseApplicationRequest.getTenantEmail());
            leaseApplication.setTenantBirthday(leaseApplicationRequest.getTenantBirthday());
            leaseApplication.setLeaseStartDate(leaseApplicationRequest.getLeaseStartDate());
            leaseApplication.setLeaseEndDate(leaseApplicationRequest.getLeaseEndDate());
            leaseApplication.setNumOfOccupants(leaseApplicationRequest.getNumOfOccupants());
            leaseApplication.setApplicationStatus(leaseApplicationRequest.getApplicationStatus());

            // save the updated lease
            LeaseApplication updatedLeaseApplication = leaseApplicationRepository.save(leaseApplication);

            //LeaseApplicationEvent event = new LeaseApplicationEvent(updatedLeaseApplication.getId(), LeaseApplicationEvent.EventType.UPDATE, updatedLeaseApplication);
            //kafkaTemplate.send("leaseApplicationsTopic",event);

            // convert the updated lease to response object
            log.info("lease Application {} updated", updatedLeaseApplication);
            return mapToLeaseApplicationResponse(updatedLeaseApplication);
        } else {
            log.info("lease Application Id {} not found.", leaseApplicationId);
            return null;
        }
    }

    public boolean deleteLeaseApplicationById(Long leaseApplicationId) {
        if (leaseApplicationRepository.existsById(leaseApplicationId)) {
            leaseApplicationRepository.deleteById(leaseApplicationId);

            //LeaseApplicationEvent event = new LeaseApplicationEvent(leaseApplicationId, LeaseApplicationEvent.EventType.DELETE, null);
            //kafkaTemplate.send("leaseApplicationsTopic",event);

            log.info("lease Application Id {} deleted", leaseApplicationId);
            return true; // delete operation executed successfully
        } else {
            log.info("lease Application Id {} not found.", leaseApplicationId);

            return false; // lease not found
        }
    }
}
