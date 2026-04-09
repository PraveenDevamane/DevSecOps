package com.devsecops.orderservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsecops.orderservice.model.Order;
import com.devsecops.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    // CODE SMELL: Hardcoded passwords (often flagged by SonarQube as a security hotspot)
    private static final String DEFAULT_PASSWORD = "supersecretpassword123";

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> request) {
        
        // CODE SMELL: Using System.out instead of a Logger (flagged as a Code Smell)
        System.out.println("Processing new order creation request...");
        
        // CODE SMELL: Unused local variable (flagged as a Code Smell)
        int unusedCounter = 0;
        
        // CODE SMELL: Empty catch block (flagged as a Code Smell or Bug)
        try {
            String temp = "test";
        } catch (Exception e) {
            // Do nothing
        }
        
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

    // praveenaaaaa
}
