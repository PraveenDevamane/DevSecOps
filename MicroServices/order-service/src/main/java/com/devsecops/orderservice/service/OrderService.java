package com.devsecops.orderservice.service;

import com.devsecops.orderservice.model.Order;
import com.devsecops.orderservice.model.PaymentRequest;
import com.devsecops.orderservice.model.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    private List<Order> orders = new ArrayList<>();
    private static final String PAYMENT_SERVICE_URL = "http://payment-service:8081/payments";

    public Order createOrder(String product, Double amount) {
        // Create order
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, product, amount, "PENDING");
        orders.add(order);

        // Call Payment Service to process payment
        try {
            PaymentRequest paymentRequest = new PaymentRequest(orderId, amount);
            PaymentResponse paymentResponse = restTemplate.postForObject(
                    PAYMENT_SERVICE_URL,
                    paymentRequest,
                    PaymentResponse.class
            );

            if (paymentResponse != null && "SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
                order.setStatus("CONFIRMED");
            } else {
                order.setStatus("FAILED");
            }
        } catch (Exception e) {
            // If payment service is down, mark order as pending
            System.err.println("Payment Service Error: " + e.getMessage());
            order.setStatus("PENDING");
        }

        return order;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
}
