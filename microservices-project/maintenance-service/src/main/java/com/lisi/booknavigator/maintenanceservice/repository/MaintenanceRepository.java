package com.lisi.booknavigator.maintenanceservice.repository;

import com.lisi.booknavigator.maintenanceservice.model.Maintenance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MaintenanceRepository extends MongoRepository<Maintenance, Integer> {
}
