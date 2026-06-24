package com.calmora.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "therapists")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Therapist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer experience;

    private String specialization;

    private String imageUrl;

    private String bio;

    private String email;
    
    private Integer clientsServed;

    private String consultationFee;

    private List<String> qualifications;

    private List<String> specializations;
}
