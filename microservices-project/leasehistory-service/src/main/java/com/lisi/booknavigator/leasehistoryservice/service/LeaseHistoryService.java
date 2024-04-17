package com.lisi.booknavigator.leasehistoryservice.service;

import com.lisi.booknavigator.leasehistoryservice.dto.LeaseHistoryRequest;
import com.lisi.booknavigator.leasehistoryservice.dto.LeaseHistoryResponse;
import com.lisi.booknavigator.leasehistoryservice.event.LeaseHistoryEvent;
import com.lisi.booknavigator.leasehistoryservice.model.LeaseHistory;
import com.lisi.booknavigator.leasehistoryservice.repository.LeaseHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaseHistoryService {

    private final LeaseHistoryRepository leaseHistoryRepository;
    private final KafkaTemplate<String, LeaseHistoryEvent> kafkaTemplate;

    public void createLeaseHistory(LeaseHistoryRequest leaseHistoryRequest){

        LeaseHistory leaseHistory = LeaseHistory.builder()

                .leaseId(leaseHistoryRequest.getLeaseId())
                .changeDate(leaseHistoryRequest.getChangeDate())
                .changeType(leaseHistoryRequest.getChangeType())
                .previousTerms(leaseHistoryRequest.getPreviousTerms())
                .newTerms(leaseHistoryRequest.getNewTerms())
                .build();

        LeaseHistory savedLeaseHistory = leaseHistoryRepository.save(leaseHistory);

        LeaseHistoryEvent event = new LeaseHistoryEvent(String.valueOf(savedLeaseHistory.getHistoryId()), LeaseHistoryEvent.EventType.CREATE, savedLeaseHistory);
        kafkaTemplate.send("LeaseHistoryTopic",event);

        log.info("LeaseHistory {} is saved", leaseHistory.getHistoryId());
    }

    public List<LeaseHistoryResponse> getAllLeaseHistories(){
        List<LeaseHistory> leaseHistories = leaseHistoryRepository.findAll();

        log.info("Retrieved {} Lease Histories", leaseHistories.size());

        return leaseHistories.stream().map(this::mapToLeaseHistoryResponse).toList();
    }

    private LeaseHistoryResponse mapToLeaseHistoryResponse(LeaseHistory leaseHistory) {
        return LeaseHistoryResponse.builder()
                .historyId(leaseHistory.getHistoryId())
                .leaseId(leaseHistory.getLeaseId())
                .changeDate(leaseHistory.getChangeDate())
                .changeType(leaseHistory.getChangeType())
                .previousTerms(leaseHistory.getPreviousTerms())
                .newTerms(leaseHistory.getNewTerms())
                .build();

    }

    public LeaseHistoryResponse getLeaseHistoryById(String historyId) {
        Optional<LeaseHistory> leaseHistoryOpt = leaseHistoryRepository.findById(historyId);
        if (leaseHistoryOpt.isPresent()) {
            LeaseHistory leaseHistory = leaseHistoryOpt.get();
            log.info("Lease History {} retrieved", leaseHistory);
            return mapToLeaseHistoryResponse(leaseHistory);
        } else {
            log.info("Lease History ID {} not found.", historyId);
            return null;
        }
    }

    public LeaseHistoryResponse updatePropertyById(String historyId, LeaseHistoryRequest leaseHistoryRequest) {
        Optional<LeaseHistory> leaseHistoryOpt = leaseHistoryRepository.findById(historyId);
        if (leaseHistoryOpt.isPresent()) {
            LeaseHistory leaseHistory = leaseHistoryOpt.get();

            //update Lease History fields
            leaseHistory.setLeaseId(leaseHistoryRequest.getLeaseId());
            leaseHistory.setChangeDate(leaseHistoryRequest.getChangeDate());
            leaseHistory.setChangeType(leaseHistoryRequest.getChangeType());
            leaseHistory.setPreviousTerms(leaseHistoryRequest.getPreviousTerms());
            leaseHistory.setNewTerms(leaseHistoryRequest.getNewTerms());


            // save the updated Lease History
            LeaseHistory updatedLeaseHistory = leaseHistoryRepository.save(leaseHistory);

            // convert the updated lease History to response object
            log.info("Lease History {} updated", leaseHistory);
            return mapToLeaseHistoryResponse(updatedLeaseHistory);
        } else {
            log.info("Lease History ID {} not found.", historyId);
            return null;
        }
    }

    public boolean deleteLeaseHistoryById(String historyId) {
        if (leaseHistoryRepository.existsById(historyId)) {
            leaseHistoryRepository.deleteById(historyId);
            log.info("Lease History ID {} deleted", historyId);
            return true; // delete operation executed successfully
        } else {
            log.info("Lease History ID {} not found.", historyId);

            return false; // Lease history not found
        }
    }
}