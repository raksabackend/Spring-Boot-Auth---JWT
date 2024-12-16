package com.example.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    @RabbitListener(queues =  "myQ")
    public void receiveMessage(String message) {
        System.out.println(message + " received");
    }

}

