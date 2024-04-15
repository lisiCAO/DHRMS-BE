package com.lisi.booknavigator.leasehistoryservice.repository;

import com.lisi.booknavigator.leasehistoryservice.model.LeaseHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeaseHistoryRepository extends MongoRepository<LeaseHistory, String> {
}
