package com.lisi.booknavigator.paymentservice.service;

import com.stripe.model.Charge;
import com.stripe.model.Token;
import jakarta.annotation.PostConstruct;
import com.lisi.booknavigator.paymentservice.dto.PaymentRequest;
import com.lisi.booknavigator.paymentservice.dto.PaymentResponse;
import com.lisi.booknavigator.paymentservice.dto.TokenChargeRequest;
import com.lisi.booknavigator.paymentservice.event.PaymentEvent;
import com.lisi.booknavigator.paymentservice.model.Payment;
import com.lisi.booknavigator.paymentservice.model.PaymentStatus;
import com.lisi.booknavigator.paymentservice.model.PaymentType;
import com.lisi.booknavigator.paymentservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public void chargeCreditCard(String token, double amount,Long leaseId,Long landLordId, Long paidByUserId, PaymentRequest paymentRequest)  {
        LocalDateTime now = LocalDateTime.now();

        // Define the date time formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the LocalDateTime object using the formatter
        String formattedDateTime = now.format(formatter);
        try {
            Map<String, Object> chargeParams = new HashMap<String, Object>();

            chargeParams.put("amount", (int)(amount * 100L));
            chargeParams.put("currency", "USD");
            float amountValue = (float) amount;

            if (token.contains("error")) {
                // Handle the error in the failed section
                Payment payment = Payment.builder()
                        .leaseId(leaseId)
                        .landLordId(landLordId)
                        .paidByUserId(paidByUserId)
                        .amount(amountValue)
                        .paymentDate(formattedDateTime)
                        .paymentMethod(PaymentType.CreditCard)
                        .status(PaymentStatus.Failed)
                        .build();

                Payment savedPayment = paymentRepository.save(payment);
                log.info("Payment {} failed: {}", savedPayment.getPaymentId(), token);
            } else {
                // Proceed with charging the card


                chargeParams.put("source", token);
                Charge charge = Charge.create(chargeParams);



                Payment payment = Payment.builder()
                        .leaseId(leaseId)
                        .landLordId(landLordId)
                        .paidByUserId(paidByUserId)
                        .amount(amountValue)
                        .paymentDate(formattedDateTime)
                        .paymentMethod(PaymentType.CreditCard)
                        .status(charge.getPaid() ? PaymentStatus.Completed : PaymentStatus.Failed)
                        .build();

                Payment savedPayment = paymentRepository.save(payment);
                log.info("Payment {} is saved", savedPayment.getPaymentId());
            }

        } catch(Exception e) {
            // Handle Stripe exception
            log.error("Error creating Payment: " + e.getMessage());
            // Return appropriate response or throw exception
        }

    }







    public List<PaymentResponse> getAllPayments(){
        List<Payment> payments = paymentRepository.findAll();

        log.info("Retrieved {} payments", payments.size());

        return payments.stream().map(this::mapToPaymentResponse).toList();
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .leaseId(payment.getLeaseId())
                .landLordId(payment.getLandLordId())
                .paidByUserId(payment.getPaidByUserId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .build();
    }

    public PaymentResponse getPaymentById(Integer paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            log.info("Payment {} retrieved", payment);
            return mapToPaymentResponse(payment);
        } else {
            log.info("Payment ID {} not found.", paymentId);
            return null;
        }
    }

    public PaymentResponse updatePaymentById(Integer paymentId, PaymentRequest paymentRequest) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();

            //update Payment fields
            payment.setLeaseId(paymentRequest.getLeaseId());
            payment.setPaidByUserId(paymentRequest.getPaidByUserId());
            payment.setAmount(paymentRequest.getAmount());
            payment.setPaymentDate(paymentRequest.getPaymentDate());
            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
            payment.setStatus(paymentRequest.getStatus());


            // save the updated Payment
            Payment updatedPayment = paymentRepository.save(payment);

            PaymentEvent event = new PaymentEvent(updatedPayment.getPaymentId(), PaymentEvent.EventType.UPDATE, updatedPayment);


            // convert the updated Payment to response object
            log.info("Payment {} updated", payment);
            return mapToPaymentResponse(updatedPayment);
        } else {
            log.info("Payment ID {} not found.", paymentId);
            return null;
        }
    }


}