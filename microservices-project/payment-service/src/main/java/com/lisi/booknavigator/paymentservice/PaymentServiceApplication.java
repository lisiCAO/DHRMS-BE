package com.lisi.booknavigator.paymentservice;

import com.lisi.booknavigator.paymentservice.dto.PaymentRequest;
import com.lisi.booknavigator.paymentservice.model.PaymentStatus;
import com.lisi.booknavigator.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class PaymentServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(PaymentServiceApplication.class, args);
        }

    @Bean
    CommandLineRunner run(PaymentService paymentService) {
        return args -> {
            if (paymentService.getAllPayments().isEmpty()) {
                // Creating mock Payment 1
                log.info("No Payments found in the database. Creating mock Payment Request.");
                PaymentRequest paymentRequest1 = new PaymentRequest(
                        4,
                        24,
                        "Bulb change",
                        "April 20, 2024",
                        "Open"
                );



                // Saving mock properties to the database
                paymentService.createPayment(paymentRequest1);
                //PaymentService.createPayment(paymentRequest2);
                log.info("Mock Payment created successfully.");
            }
        };
    }
}