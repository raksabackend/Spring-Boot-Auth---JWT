package com.example.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client1-orderservice", url = "http://localhost:6666")
public interface OrderServiceClient {

    @GetMapping("/order/{id}")
    String getOrderById(@PathVariable("id") int id);


}
