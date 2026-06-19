package com.calmora.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "therapists")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Therapist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String specialization;

    private Integer experience;

    private String imageUrl;

    private String bio;

    private String email;

    private String phoneNumber;

    private Double rating;
}
