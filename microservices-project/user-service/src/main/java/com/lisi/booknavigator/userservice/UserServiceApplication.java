package com.lisi.booknavigator.userservice;

import com.lisi.booknavigator.userservice.dto.UserRequest;
import com.lisi.booknavigator.userservice.model.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.lisi.booknavigator.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SpringBootApplication
@Slf4j
public class UserServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(UserServiceApplication.class, args);
        }

    @Bean
    CommandLineRunner run(UserService userService) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        return args -> {
            if (UserService.getAllUsers().isEmpty()) {
                // Creating mock Lease History 1
                log.info("No Lease History found in the database. Creating mock Lease History.");
                Date changeDate = dateFormat.parse("April 10, 2026");
                LeaseHistoryRequest leaseHistoryRequest1 = new UserRequest(

                        Long.valueOf("4"),
                        changeDate,
                        LeaseHistoryType.RENEWAL,
                        "2 years",
                        "3 years"
                );

                // Creating mock Lease History 2
                UserRequest leaseHistoryRequest2 = new UserRequest(
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