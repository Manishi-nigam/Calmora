package com.calmora.controller;


import com.calmora.DTO.RecommendationResponseDTO;
import com.calmora.service.RecommendationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public RecommendationResponseDTO getRecommendations() {
        return recommendationService.getRecommendations();
    }
}