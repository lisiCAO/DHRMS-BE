package com.lisi.booknavigator.paymentservice.service;

import com.lisi.booknavigator.paymentservice.dto.PaymentRequest;
import com.lisi.booknavigator.paymentservice.dto.PaymentResponse;
import com.lisi.booknavigator.paymentservice.event.PaymentEvent;

import com.lisi.booknavigator.paymentservice.model.Payment;
import com.lisi.booknavigator.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void createPayment(PaymentRequest paymentRequest){

        Payment payment = Payment.builder()
                .propertyId(paymentRequest.getPropertyId())
                .requestedByUserId(paymentRequest.getRequestedByUserId())
                .description(paymentRequest.getDescription())
                .requestDate(paymentRequest.getRequestDate())
                .status(paymentRequest.getStatus())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        PaymentEvent event = new PaymentEvent(savedPayment.getPaymentId(), PaymentEvent.EventType.CREATE, savedPayment);
        kafkaTemplate.send("paymentsTopic",event);

        log.info("payment {} is saved", payment.getPaymentId());
    }

    public List<PaymentResponse> getAllPayments(){
        List<Payment> payments = paymentRepository.findAll();

        log.info("Retrieved {} payments", payments.size());

        return payments.stream().map(this::mapToPaymentResponse).toList();
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .requestId(payment.getRequestId())
                .propertyId(payment.getPropertyId())
                .requestedByUserId(payment.getRequestedByUserId())
                .description(payment.getDescription())
                .requestDate(payment.getRequestDate())
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
            payment.setPropertyId(paymentRequest.getPropertyId());
            payment.setRequestedByUserId(paymentRequest.getRequestedByUserId());
            payment.setDescription(paymentRequest.getDescription());
            payment.setRequestDate(paymentRequest.getRequestDate());
            payment.setStatus(paymentRequest.getStatus());


            // save the updated Payment
            Payment updatedPayment = paymentRepository.save(payment);

            PaymentEvent event = new PaymentEvent(updatedPayment.getPaymentId(), PaymentEvent.EventType.UPDATE, updatedPayment);
            kafkaTemplate.send("propertiesTopic",event);

            // convert the updated Payment to response object
            log.info("Payment {} updated", payment);
            return mapToPaymentResponse(updatedPayment);
        } else {
            log.info("Payment ID {} not found.", paymentId);
            return null;
        }
    }

    public boolean deletePaymentById(Integer paymentId) {
        if (paymentRepository.existsById(paymentId)) {
            paymentRepository.deleteById(paymentId);

            PaymentEvent event = new PaymentEvent(paymentId, PaymentEvent.EventType.DELETE, null);
            kafkaTemplate.send("PaymentsTopic",event);

            log.info("Payment ID {} deleted", paymentId);
            return true; // delete operation executed successfully
        } else {
            log.info("Payment ID {} not found.", paymentId);

            return false; // Payment not found
        }
    }
}