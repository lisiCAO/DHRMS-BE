package com.lisi.booknavigator.leaseservice.service;

import com.lisi.booknavigator.leaseservice.dto.LeaseApplicationRequest;
import com.lisi.booknavigator.leaseservice.dto.LeaseApplicationResponse;
import com.lisi.booknavigator.leaseservice.event.LeaseApplicationEvent;
import com.lisi.booknavigator.leaseservice.model.LeaseApplication;
import com.lisi.booknavigator.leaseservice.repository.LeaseApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaseApplicationService {

    private final LeaseApplicationRepository leaseApplicationRepository;
    private final KafkaTemplate<String, LeaseApplicationEvent> kafkaTemplate;

    public LeaseApplicationResponse createLeaseApplication(LeaseApplicationRequest leaseApplicationRequest){

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

        LeaseApplicationEvent event = new LeaseApplicationEvent(savedLeaseApplication.getId(), LeaseApplicationEvent.EventType.CREATE, savedLeaseApplication);
        kafkaTemplate.send("leaseApplicationsTopic",event);

        log.info("lease Application {} is saved", savedLeaseApplication.getId());

        return mapToLeaseApplicationResponse(savedLeaseApplication);
    }

    public List<LeaseApplicationResponse> getAllLeaseApplication(){
        List<LeaseApplication> leaseApplications = leaseApplicationRepository.findAll();

        log.info("Retrieved {} LeaseApplication", leaseApplications.size());

        return leaseApplications.stream().map(this::mapToLeaseApplicationResponse).toList();
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

            LeaseApplicationEvent event = new LeaseApplicationEvent(updatedLeaseApplication.getId(), LeaseApplicationEvent.EventType.UPDATE, updatedLeaseApplication);
            kafkaTemplate.send("leaseApplicationsTopic",event);

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

            LeaseApplicationEvent event = new LeaseApplicationEvent(leaseApplicationId, LeaseApplicationEvent.EventType.DELETE, null);
            kafkaTemplate.send("leaseApplicationsTopic",event);

            log.info("lease Application Id {} deleted", leaseApplicationId);
            return true; // delete operation executed successfully
        } else {
            log.info("lease Application Id {} not found.", leaseApplicationId);

            return false; // lease not found
        }
    }
}
