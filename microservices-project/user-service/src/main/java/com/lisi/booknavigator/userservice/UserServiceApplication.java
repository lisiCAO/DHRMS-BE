package com.lisi.booknavigator.userservice;

import com.lisi.booknavigator.userservice.dto.UserRequest;
import com.lisi.booknavigator.userservice.model.UserType;
import com.lisi.booknavigator.userservice.model.User_profile;
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
            if (userService.getAllUsers().isEmpty()) {
                // Creating mock Lease History 1
                log.info("No Lease History found in the database. Creating mock Lease History.");
                Date changeDate = dateFormat.parse("April 10, 2026");
                UserRequest userRequest1 = new UserRequest(
                        "username1",
                        "username1@email.com",
                        "This is passwordhash",
                        UserType.TENANT,
                        new User_profile( "user", "name", "1234569999", "12345 Rue Haymen , montreal, canada", "h4j6l9", 12424L),
                        "English"

                );



                // Saving mock properties to the database
                userService.createUser(userRequest1);
                //userService.createUser(userRequest2);
                log.info("Mock Lease History created successfully.");
            }
        };
    }
}