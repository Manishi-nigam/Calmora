package com.calmora.DTO.mood;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MoodResponseDTO {
    private Long id;
    private String moodType;
    private LocalDateTime createdAt;
}
