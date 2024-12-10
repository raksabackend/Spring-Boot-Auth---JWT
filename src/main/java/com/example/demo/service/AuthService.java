package com.example.demo.service;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Random;

@Service
public class AuthService {

    private final OrderServiceClient orderServiceClient;

    private final Random random = new Random();

    @Autowired
    public AuthService(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    public String fetchOrder(int orderId) {
        return orderServiceClient.getOrderById(orderId);
    }

    @CircuitBreaker(name = "client1-orderservice", fallbackMethod = "fallbackOrder")
    @Retry(name = "client1-orderservice", fallbackMethod = "fallbackMethod")
    public String processOrder(String request) {
        // Simulate a call that might fail or take too long
        if (random.nextInt(10) < 3) { // 30% chance to fail
            throw new RuntimeException("Simulated external service failure");
        }

        return callExternalService(request);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Service is unavailable")
    public String fallbackOrder(String request, Throwable throwable) {
        // Fallback logic when the circuit breaker is triggered
        return "Fallback response: Unable to process order.";
    }

    private String callExternalService(String request) {
        // Simulate external service call logic here
        return  "Order processed successfully.";
    }

}

