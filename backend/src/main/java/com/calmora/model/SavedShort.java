package com.calmora.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;   
import lombok.Data;
import lombok.NoArgsConstructor;    
import java.time.LocalDateTime;
import com.calmora.model.User;

@Entity
@Table(name = "saved_shorts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedShort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private ShortVideo shortVideo;

    private LocalDateTime savedAt;
}
