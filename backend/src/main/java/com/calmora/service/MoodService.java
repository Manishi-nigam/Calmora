package com.calmora.service;

import com.calmora.DTO.mood.MoodRequestDTO;
import com.calmora.DTO.mood.MoodResponseDTO;
import com.calmora.model.Mood;
import com.calmora.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.time.LocalDateTime;

@Service
public class MoodService {

    @Autowired
    private MoodRepository moodRepository;

    public MoodResponseDTO addMood(MoodRequestDTO request) {

        Mood mood = new Mood();

        mood.setMoodType(request.getMoodType());
        mood.setCreatedAt(LocalDateTime.now());

        Mood savedMood = moodRepository.save(mood);

        return new MoodResponseDTO(
                savedMood.getId(),
                savedMood.getMoodType(),
                savedMood.getCreatedAt()
        );
    }

    public List<MoodResponseDTO> getAllMoods() {

        return moodRepository.findAll()
                .stream()
                .map(mood -> new MoodResponseDTO(
                        mood.getId(),
                        mood.getMoodType(),
                        mood.getCreatedAt()
                ))
                .toList();
    }

}
