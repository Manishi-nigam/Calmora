package com.calmora.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.calmora.model.Wisdom;

@Repository
public interface WisdomRepository extends JpaRepository<Wisdom, Long> {
    
}
