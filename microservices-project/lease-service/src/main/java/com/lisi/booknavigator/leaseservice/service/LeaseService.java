package com.lisi.booknavigator.leaseservice.service;

import com.lisi.booknavigator.leaseservice.dto.LeaseRequest;
import com.lisi.booknavigator.leaseservice.dto.LeaseResponse;
//import com.lisi.booknavigator.leaseservice.event.leaseEvent;
import com.lisi.booknavigator.leaseservice.event.LeaseEvent;

import com.lisi.booknavigator.leaseservice.model.Lease;
import com.lisi.booknavigator.leaseservice.repository.LeaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//import org.springframework.kafka.core.KafkaTemplate;
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
        kafkaTemplate.send("LeasesTopic",event);

        log.info("lease {} is saved", lease.getId());
    }

    public List<LeaseResponse> getAllLease(){
        List<Lease> leases = leaseRepository.findAll();

        log.info("Retrieved {} properties", leases.size());

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

    public LeaseResponse getLeaseById(String leaseId) {
        Optional<Lease> leaseOpt = leaseRepository.findById(leaseId);
        if (leaseOpt.isPresent()) {
            Lease lease = leaseOpt.get();
            log.info("lease {} retrieved", lease);
            return mapToLeaseResponse(lease);
        } else {
            log.info("lease ID {} not found.", leaseId);
            return null;
        }
    }

    public LeaseResponse updateLeaseById(String leaseId, LeaseRequest leaseRequest) {
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

            // convert the updated lease to response object
            log.info("lease {} updated", lease);
            return mapToLeaseResponse(updatedLease);
        } else {
            log.info("lease ID {} not found.", leaseId);
            return null;
        }
    }

    public boolean deleteLeaseById(String leaseId) {
        if (leaseRepository.existsById(leaseId)) {
            leaseRepository.deleteById(leaseId);
            log.info("lease ID {} deleted", leaseId);
            return true; // delete operation executed successfully
        } else {
            log.info("lease ID {} not found.", leaseId);

            return false; // lease not found
        }
    }
}