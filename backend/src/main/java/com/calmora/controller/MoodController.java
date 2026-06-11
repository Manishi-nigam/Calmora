package com.calmora.controller;

import com.calmora.DTO.mood.MoodRequestDTO;
import com.calmora.DTO.mood.MoodResponseDTO;
import com.calmora.model.Mood;
import com.calmora.service.MoodService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mood")
public class MoodController {

    private final MoodService moodService;


    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }


    @PostMapping
    public MoodResponseDTO addMood(@RequestBody MoodRequestDTO mood) {
        return moodService.addMood(mood);
    }


    @GetMapping
    public List<MoodResponseDTO> getAllMoods() {
        return moodService.getAllMoods();
    }
}
