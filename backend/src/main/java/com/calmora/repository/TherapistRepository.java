package com.calmora.repository;

import com.calmora.model.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TherapistRepository
        extends JpaRepository<Therapist, Long> {
}
