package com.devsecops.orderservice.controller;

import com.devsecops.orderservice.model.Order;
import com.devsecops.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    // Intentionally dummy test secret for CI validation (GitLeaks rule should catch this)
private static final String DUMMY_API_KEY = "DUMMY_GITLEAKS_SHOULD_FAIL_2026_04_06";

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> request) {
        String product = (String) request.get("product");
        Double amount = ((Number) request.get("amount")).doubleValue();

        Order order = orderService.createOrder(product, amount);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}


//api_key:"AIsdcbgfvbdshjfhbfvhjbvdshjfsa";
