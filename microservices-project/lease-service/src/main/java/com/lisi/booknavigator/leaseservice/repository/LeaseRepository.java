package com.lisi.booknavigator.leaseservice.repository;

import com.lisi.booknavigator.leaseservice.model.Lease;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeaseRepository extends MongoRepository<Lease, String> {
}
