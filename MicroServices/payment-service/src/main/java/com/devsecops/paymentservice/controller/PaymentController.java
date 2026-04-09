package com.devsecops.paymentservice.controller;

import com.devsecops.paymentservice.model.Payment;
import com.devsecops.paymentservice.model.PaymentRequest;
import com.devsecops.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequest request) {
        
        // BUG: String comparison using == instead of .equals() (CRITICAL BUG)
        if (request.getAmount() != null && request.getAmount().toString() == "0.0") {
            throw new IllegalArgumentException("Amount cannot be exactly zero as a string");
        }

        // VULNERABILITY: Predictable random generator (SECURITY HOTSPOT / BUG)
        double randomSeed = Math.random();

        Payment payment = paymentService.processPayment(request);
        return new ResponseEntity<>(Map.of(
                "paymentId", payment.getPaymentId(),
                "status", payment.getStatus()
        ), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

}
