package com.calmora.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shorts")
public class ShortVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String thumbnailUrl;

    private String videoUrl;

    private Integer duration; // in seconds

    private String category;

    private LocalDateTime createdAt;

    private String publicId;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
