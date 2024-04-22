package com.lisi.booknavigator.leaseservice;

import com.lisi.booknavigator.leaseservice.dto.LeaseApplicationRequest;
import com.lisi.booknavigator.leaseservice.service.LeaseApplicationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.lisi.booknavigator.leaseservice.dto.LeaseRequest;
import com.lisi.booknavigator.leaseservice.service.LeaseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.Month;
import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class LeaseServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(LeaseServiceApplication.class, args);
        }

    @Bean
    CommandLineRunner run(LeaseService leaseService, LeaseApplicationService leaseApplicationService) {
        return args -> {
            if (leaseService.getAllLease().isEmpty()) {
                // Creating mock lease 1
                log.info("No Lease found in the database. Creating mock Lease.");
                LeaseRequest leaseRequest1 = null;
                LeaseRequest leaseRequest2 = null;

                    // Creating Date objects for startDate and endDate
                    LocalDateTime startDate1 = LocalDateTime.of(2024, Month.APRIL, 1, 15, 30);
                    LocalDateTime startDate2 = LocalDateTime.of(2024, Month.MAY, 1, 15, 30);
                    LocalDateTime endDate1 = LocalDateTime.of(2025, Month.APRIL, 1, 15, 30);
                    LocalDateTime endDate2 = LocalDateTime.of(2026, Month.MAY, 1, 15, 30);


                    // Creating mock Lease1
                    leaseRequest1 = new LeaseRequest(
                            "2",
                            Long.valueOf("100"),
                            startDate1,
                            endDate1,
                            Float.valueOf("1050"),
                            Float.valueOf("500"),
                            "ACTIVE",
                            "2 years"
                    );

                    // Creating mock Lease 2
                    leaseRequest2 = new LeaseRequest(
                            "3",
                            Long.valueOf("101"),
                            startDate1,
                            endDate2,
                            Float.valueOf("1250"),
                            Float.valueOf("700"),
                            "ACTIVE",
                            "5 years"
                    );



                // Saving mock properties to the database
                leaseService.createLease(leaseRequest1);
                leaseService.createLease(leaseRequest2);
                log.info("Mock Lease created successfully.");
            }

            if (leaseApplicationService.getAllLeaseApplication().isEmpty()) {
                // Creating mock lease 1
                log.info("No Lease Application found in the database. Creating mock Lease Application.");
                LeaseApplicationRequest leaseApplicationRequest1 = null;
                LeaseApplicationRequest leaseApplicationRequest2 = null;

                // Creating Date objects for startDate and endDate
                LocalDateTime startDate1 = LocalDateTime.of(2024, Month.APRIL, 1, 15, 30);
                LocalDateTime startDate2 = LocalDateTime.of(2024, Month.MAY, 1, 15, 30);
                LocalDateTime endDate1 = LocalDateTime.of(2025, Month.APRIL, 1, 15, 30);
                LocalDateTime endDate2 = LocalDateTime.of(2026, Month.MAY, 1, 15, 30);
                LocalDateTime birthDate1 = LocalDateTime.of(2020, Month.APRIL, 1, 15, 30);
                LocalDateTime birthDate2 = LocalDateTime.of(1995, Month.APRIL, 1, 15, 30);


                // Creating mock Lease1
                leaseApplicationRequest1 = new LeaseApplicationRequest(
                        "1",
                        Long.valueOf("100"),
                        "Joe",
                        "joe@email.com",
                        birthDate1,
                        startDate1,
                        endDate1,
                        1, "Pending"
                );

                leaseApplicationRequest2 = new LeaseApplicationRequest(
                        "2",
                        Long.valueOf("105"),
                        "John",
                        "john@email.com",
                        birthDate2,
                        startDate2,
                        endDate2,
                        1, "Denied"
                );

                // Saving mock properties to the database
                leaseApplicationService.createLeaseApplication(leaseApplicationRequest1);
                leaseApplicationService.createLeaseApplication(leaseApplicationRequest2);
                log.info("Mock Lease Application created successfully.");
            }
        };
    }
}