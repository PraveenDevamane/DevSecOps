package com.devsecops.paymentservice.service;

import com.devsecops.paymentservice.model.Payment;
import com.devsecops.paymentservice.model.PaymentRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {

    private List<Payment> payments = new ArrayList<>();
    private Random random = new Random();

    public Payment processPayment(PaymentRequest request) {
        String paymentId = UUID.randomUUID().toString();
        
        // Simulate payment processing - 80% success rate
        String status = random.nextDouble() < 0.8 ? "SUCCESS" : "FAILED";
        
        Payment payment = new Payment(paymentId, request.getOrderId(), request.getAmount(), status);
        payments.add(payment);

        System.out.println("Payment processed: " + payment);
        return payment;
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }
}
