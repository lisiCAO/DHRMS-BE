package com.lisi.booknavigator.paymentservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.model.Charge;
import jakarta.servlet.http.HttpServletRequest;
import com.lisi.booknavigator.paymentservice.dto.PaymentRequest;
import com.lisi.booknavigator.paymentservice.dto.PaymentResponse;
import com.lisi.booknavigator.paymentservice.dto.TokenChargeRequest;
import com.lisi.booknavigator.paymentservice.model.Payment;
import com.lisi.booknavigator.paymentservice.model.PaymentStatus;
import com.lisi.booknavigator.paymentservice.repository.PaymentRepository;
import com.lisi.booknavigator.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments/")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Object> chargeCard(@RequestBody Map<String, Object> payload ) {

            String token = (String) payload.get("token");
            Double amountValue = Double.parseDouble(payload.get("amount").toString());
                // Charge the card
            //String token = request.getHeader("token");
            System.out.println(token);
            //Double amountValue = Double.parseDouble(amount);
            System.out.println(amountValue);
            try {   PaymentRequest paymentRequest = new PaymentRequest();
             paymentService.chargeCreditCard(token, amountValue, paymentRequest  );

            // If the card is successfully charged, create the payment

                //paymentService.createPayment(paymentRequest);
                log.info("Payment created successfully.");
                return ResponseEntity.status(HttpStatus.CREATED).body("Payment created successfully.");

        } catch (Exception e) {
            log.error("Error charging card and creating payment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error charging card and creating payment: " + e.getMessage());
        }
    }





    @GetMapping
    public ResponseEntity<Object> getAllPayments() {
        try {
            List<PaymentResponse> payments = paymentService.getAllPayments();
            if (payments.isEmpty()) {
                log.info("No Payments found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Payments found.");
            } else {
                log.info("Retrieved {} Payments", payments.size());
                return ResponseEntity.ok(payments);
            }
        } catch (Exception e) {
            log.error("Error retrieving Payments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Payments: " + e.getMessage());
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Object> getPaymentById(@PathVariable Integer paymentId) {
        try {
            PaymentResponse payment = paymentService.getPaymentById(paymentId);
            if (payment != null) {
                log.info("Payment Id {} retrieved successfully.", paymentId);
                return ResponseEntity.ok(payment);
            } else {
                log.info("Payment ID {} not found.", paymentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving Payment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Payment: " + e.getMessage());
        }
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<Object> updatePaymentById(@PathVariable Integer paymentId, @RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentResponse updatedPayment = paymentService.updatePaymentById(paymentId, paymentRequest);
            if (updatedPayment != null) {
                log.info("Payment Id {} updated successfully.", paymentId);
                return ResponseEntity.ok("Payment updated successfully.");
            } else {
                log.info("Payment ID {} not found.", paymentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving Payment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Payment: " + e.getMessage());
        }
    }


}
