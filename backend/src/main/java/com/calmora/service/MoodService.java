package com.calmora.service;

import com.calmora.DTO.mood.MoodRequestDTO;
import com.calmora.DTO.mood.MoodResponseDTO;
import com.calmora.model.Mood;
import com.calmora.model.User;
import com.calmora.repository.MoodRepository;
import com.calmora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.time.LocalDateTime;

@Service
public class MoodService {

    @Autowired
    private MoodRepository moodRepository;
    @Autowired
    private UserRepository UserRepository;

    public MoodResponseDTO addMood(MoodRequestDTO request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();


        Mood mood = new Mood();

        mood.setMoodType(request.getMoodType());
        mood.setCreatedAt(LocalDateTime.now());

        User user = UserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        mood.setUser(user);

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
