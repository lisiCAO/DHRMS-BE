package com.lisi.booknavigator.leaseservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.lisi.booknavigator.leaseservice.dto.LeaseRequest;
import com.lisi.booknavigator.leaseservice.service.LeaseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Locale;

@SpringBootApplication
@Slf4j
public class LeaseServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(LeaseServiceApplication.class, args);
        }

    @Bean
    CommandLineRunner run(LeaseService leaseService) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        return args -> {
            if (leaseService.getAllLease().isEmpty()) {
                // Creating mock lease 1
                log.info("No Lease found in the database. Creating mock Lease.");
                LeaseRequest leaseRequest1 = null;
                LeaseRequest leaseRequest2 = null;
                try {
                    // Creating Date objects for startDate and endDate
                    Date startDate = dateFormat.parse("April 10, 2023");
                    Date endDate1 = dateFormat.parse("April 10, 2025");
                    Date endDate2 = dateFormat.parse("April 10, 2028");

                    // Creating mock Lease1
                    leaseRequest1 = new LeaseRequest(
                            "2",
                            Long.valueOf("100"),
                            startDate,
                            endDate1,
                            Float.valueOf("1050"),
                            Float.valueOf("500"),
                            LeaseType.ACTIVE,
                            "2 years"
                    );

                    // Creating mock Lease 2
                    leaseRequest2 = new LeaseRequest(
                            "3",
                            Long.valueOf("101"),
                            startDate,
                            endDate2,
                            Float.valueOf("1250"),
                            Float.valueOf("700"),
                            LeaseType.ACTIVE,
                            "5 years"
                    );
                } catch (ParseException e) {
                    log.error("Error parsing date: " + e.getMessage());
                }


                // Saving mock properties to the database
                leaseService.createLease(leaseRequest1);
                leaseService.createLease(leaseRequest2);
                log.info("Mock Lease created successfully.");
            }
        };
    }
}