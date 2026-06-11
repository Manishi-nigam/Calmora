package com.calmora.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "moods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Allowed values: happy, sad, neutral, angry, tired
    @Column(nullable = false)
    private String moodType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
