package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceWebClient {
    private final WebClient webClient;

    @Autowired
    public OrderServiceWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:6666").build();
    }

    public OrderServiceWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getOrderById(int id) {
        return webClient.get()
                .uri("/order/{id}", id)
                .retrieve().bodyToMono(String.class);
    }

}
