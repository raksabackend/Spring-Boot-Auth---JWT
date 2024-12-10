package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    private final String BASE_URL = "http://localhost:6666";

    @Autowired
    private RestTemplate restTemplate;

    public String getOrderById(int id) {
        String url = String.format("%s/order/%d", BASE_URL, id);
        return restTemplate.getForObject(url, String.class);
    }
}
