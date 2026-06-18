package com.calmora.model;

import com.calmora.model.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private  boolean enabled=true;

    @Column(name = "created_at")
    private LocalDateTime createdAt= LocalDateTime.now();

    @OneToMany(mappedBy = "user")
    private List<Mood> moods;

    @OneToMany(mappedBy ="user" )
    private List<JournalEntry> journals;

    private String phoneNumber;

    private String gender;

    private LocalDate dateOfBirth;

    private String profileImageUrl;

}
