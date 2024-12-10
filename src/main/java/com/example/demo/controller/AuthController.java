package com.example.demo.controller;

import com.example.demo.Util.JwtUtil;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.AuthService;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderServiceClient;
import com.example.demo.service.OrderServiceWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderServiceWebClient orderServiceWebClient;


    @Autowired
    private AuthService authService;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @GetMapping("/testcalltoOrder/{id}")
    public Mono<String> getOrderFromAuthService(@PathVariable("id") int id){
        return orderServiceWebClient.getOrderById(id);
    }

    @GetMapping("/hi")
    public String hi() {
        return "Hi from Auth Service";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity user) {
        System.out.println("Hello how are you ?");
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity user) {
        UserEntity existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && user.getPassword().equalsIgnoreCase(existingUser.getPassword()) ) {
            String token = jwtUtil.generateToken(existingUser.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/test-resilience")
    public String processOrder(@RequestBody String request) {
        // Call the OrderService to process the order
        return authService.processOrder(request);
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        try {
            File dir = new File(UPLOAD_DIR);
            if(!dir.exists()){
                dir.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(new File(UPLOAD_DIR + file.getOriginalFilename()));
            fos.write(file.getBytes());
            fos.close();

            return file.getOriginalFilename();

        } catch (Exception e) {
            return "File Upload Failed";
        }
    }

}


