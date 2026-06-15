package com.calmora.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/health")
    public Map<String, Object> test() {

        return Map.of(
                "status", "UP",
                "message", "Calmora API is running successfully",
                "timestamp", LocalDateTime.now()
        );
    }
}
