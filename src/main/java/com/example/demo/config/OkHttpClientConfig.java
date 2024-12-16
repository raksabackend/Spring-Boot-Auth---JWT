package com.example.demo.config;

import okhttp3.OkHttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkHttpClientConfig {

    @Bean
    @LoadBalanced
    public OkHttpClient.Builder okHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

}
