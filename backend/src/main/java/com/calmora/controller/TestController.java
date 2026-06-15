package com.calmora.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

      @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/health")
    public Map<String, Object> health() {

        return Map.of(
                "status", "UP",
                "message", "Calmora API is running successfully",
                "timestamp", LocalDateTime.now()
        );
    }
}
