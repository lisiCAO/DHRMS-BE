package com.lisi.booknavigator.leaseservice.service;

import com.lisi.booknavigator.leaseservice.dto.LeaseRequest;
import com.lisi.booknavigator.leaseservice.dto.LeaseResponse;
import com.lisi.booknavigator.leaseservice.event.LeaseEvent;

import com.lisi.booknavigator.leaseservice.model.Lease;
import com.lisi.booknavigator.leaseservice.repository.LeaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final KafkaTemplate<String, LeaseEvent> kafkaTemplate;

    public void createLease(LeaseRequest leaseRequest){

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

        LeaseEvent event = new LeaseEvent(savedLease.getId(), LeaseEvent.EventType.CREATE, savedLease);
        kafkaTemplate.send("leasesTopic",event);

        log.info("lease {} is saved", savedLease.getId());
    }

    public List<LeaseResponse> getAllLease(){
        List<Lease> leases = leaseRepository.findAll();

        log.info("Retrieved {} leases", leases.size());

        return leases.stream().map(this::mapToLeaseResponse).toList();
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

            LeaseEvent event = new LeaseEvent(updatedLease.getId(), LeaseEvent.EventType.UPDATE, updatedLease);
            kafkaTemplate.send("leasesTopic",event);

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

            LeaseEvent event = new LeaseEvent(leaseId, LeaseEvent.EventType.DELETE, null);
            kafkaTemplate.send("leasesTopic",event);

            log.info("lease Id {} deleted", leaseId);
            return true; // delete operation executed successfully
        } else {
            log.info("lease Id {} not found.", leaseId);

            return false; // lease not found
        }
    }
}