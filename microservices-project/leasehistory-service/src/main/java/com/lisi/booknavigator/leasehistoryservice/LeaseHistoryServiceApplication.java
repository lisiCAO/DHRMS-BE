package com.lisi.booknavigator.leasehistoryservice;

import com.lisi.booknavigator.leasehistoryservice.dto.LeaseHistoryRequest;
import com.lisi.booknavigator.leasehistoryservice.model.LeaseHistoryType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.lisi.booknavigator.leasehistoryservice.service.LeaseHistoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@Slf4j
public class LeaseHistoryServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(LeaseHistoryServiceApplication.class, args);
        }

    @Bean
    CommandLineRunner run(LeaseHistoryService propertyService) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        return args -> {
            if (propertyService.getAllLeaseHistories().isEmpty()) {
                // Creating mock Lease History 1
                log.info("No Lease History found in the database. Creating mock Lease History.");
                Date changeDate = dateFormat.parse("April 10, 2026");
                LeaseHistoryRequest leaseHistoryRequest1 = new LeaseHistoryRequest(

                        Long.valueOf("4"),
                        changeDate,
                        LeaseHistoryType.RENEWAL,
                        "2 years",
                        "3 years"
                );

                // Creating mock Lease History 2
                LeaseHistoryRequest leaseHistoryRequest2 = new LeaseHistoryRequest(
                        Long.valueOf("4"),
                        changeDate,
                        LeaseHistoryType.TERMINATION,
                        "2 years",
                        "Terminated"
                );

                // Saving mock properties to the database
                propertyService.createLeaseHistory(leaseHistoryRequest1);
                propertyService.createLeaseHistory(leaseHistoryRequest2);
                log.info("Mock Lease History created successfully.");
            }
        };
    }
}