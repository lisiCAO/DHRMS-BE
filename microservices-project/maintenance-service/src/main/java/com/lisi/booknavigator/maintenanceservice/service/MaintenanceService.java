package com.lisi.booknavigator.maintenanceservice.service;

import com.lisi.booknavigator.maintenanceservice.dto.MaintenanceRequest;
import com.lisi.booknavigator.maintenanceservice.dto.MaintenanceResponse;
import com.lisi.booknavigator.maintenanceservice.event.MaintenanceEvent;

import com.lisi.booknavigator.maintenanceservice.model.Maintenance;
import com.lisi.booknavigator.maintenanceservice.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final KafkaTemplate<String, MaintenanceEvent> kafkaTemplate;

    public void createMaintenance(MaintenanceRequest maintenanceRequest){

        Maintenance maintenance = Maintenance.builder()
                .propertyId(maintenanceRequest.getPropertyId())
                .requestedByUserId(maintenanceRequest.getRequestedByUserId())
                .description(maintenanceRequest.getDescription())
                .requestDate(maintenanceRequest.getRequestDate())
                .status(maintenanceRequest.getStatus())
                .build();

        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);

        MaintenanceEvent event = new MaintenanceEvent(savedMaintenance.getRequestId(), MaintenanceEvent.EventType.CREATE, savedMaintenance);
        kafkaTemplate.send("maintenancesTopic",event);

        log.info("Maintenance {} is saved", maintenance.getRequestId());
    }

    public List<MaintenanceResponse> getAllMaintenances(){
        List<Maintenance> properties = maintenanceRepository.findAll();

        log.info("Retrieved {} properties", properties.size());

        return properties.stream().map(this::mapToMaintenanceResponse).toList();
    }

    private MaintenanceResponse mapToMaintenanceResponse(Maintenance maintenance) {
        return MaintenanceResponse.builder()
                .requestId(maintenance.getRequestId())
                .propertyId(maintenance.getPropertyId())
                .requestedByUserId(maintenance.getRequestedByUserId())
                .description(maintenance.getDescription())
                .requestDate(maintenance.getRequestDate())
                .status(maintenance.getStatus())
                .build();
    }

    public MaintenanceResponse getMaintenanceById(Integer requestId) {
        Optional<Maintenance> maintenanceOpt = maintenanceRepository.findById(requestId);
        if (maintenanceOpt.isPresent()) {
            Maintenance maintenance = maintenanceOpt.get();
            log.info("Maintenance {} retrieved", maintenance);
            return mapToMaintenanceResponse(maintenance);
        } else {
            log.info("Maintenance ID {} not found.", requestId);
            return null;
        }
    }

    public MaintenanceResponse updateMaintenanceById(Integer requestId, MaintenanceRequest maintenanceRequest) {
        Optional<Maintenance> maintenanceOpt = maintenanceRepository.findById(requestId);
        if (maintenanceOpt.isPresent()) {
            Maintenance maintenance = maintenanceOpt.get();

            //update Maintenance fields
            maintenance.setPropertyId(maintenanceRequest.getPropertyId());
            maintenance.setRequestedByUserId(maintenanceRequest.getRequestedByUserId());
            maintenance.setDescription(maintenanceRequest.getDescription());
            maintenance.setRequestDate(maintenanceRequest.getRequestDate());
            maintenance.setStatus(maintenanceRequest.getStatus());


            // save the updated Maintenance
            Maintenance updatedMaintenance = maintenanceRepository.save(maintenance);

            MaintenanceEvent event = new MaintenanceEvent(updatedMaintenance.getRequestId(), MaintenanceEvent.EventType.UPDATE, updatedMaintenance);
            kafkaTemplate.send("propertiesTopic",event);

            // convert the updated Maintenance to response object
            log.info("Maintenance {} updated", maintenance);
            return mapToMaintenanceResponse(updatedMaintenance);
        } else {
            log.info("Maintenance ID {} not found.", requestId);
            return null;
        }
    }

    public boolean deleteMaintenanceById(Integer requestId) {
        if (maintenanceRepository.existsById(requestId)) {
            maintenanceRepository.deleteById(requestId);

            MaintenanceEvent event = new MaintenanceEvent(requestId, MaintenanceEvent.EventType.DELETE, null);
            kafkaTemplate.send("maintenancesTopic",event);

            log.info("Maintenance ID {} deleted", requestId);
            return true; // delete operation executed successfully
        } else {
            log.info("Maintenance ID {} not found.", requestId);

            return false; // Maintenance not found
        }
    }
}