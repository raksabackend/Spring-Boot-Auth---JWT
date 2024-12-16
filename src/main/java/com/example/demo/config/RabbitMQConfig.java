package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "myQ";

    public static final String EXCHANGE_NAME = "myExchange";

    public static final String ROUTING_KEY = "myRoutingKey";

    @Bean
    public Queue myQ() {
        return new Queue(QUEUE_NAME, true);

    }

    @Bean
    public TopicExchange myExchange() {
        return new TopicExchange(EXCHANGE_NAME);

    }

    @Bean
    public Binding binding(Queue myQ, TopicExchange myExchange) {
        return BindingBuilder.bind(myQ).to(myExchange).with(ROUTING_KEY);
    }

}

