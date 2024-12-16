package com.example.demo.controller;

import com.example.demo.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit")
public class MessegeController {


    @Autowired
    private MessageSender messageSender;

    @PostMapping("/send")
    public String sendMsg(@RequestParam String message){
        messageSender.sendMessage("myQ",message);
        return "Message sent";
    }

    
}

