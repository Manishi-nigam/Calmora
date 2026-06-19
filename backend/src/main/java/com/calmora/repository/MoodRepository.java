package com.calmora.repository;

import com.calmora.model.JournalEntry;
import com.calmora.model.Mood;
import com.calmora.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodRepository extends JpaRepository<Mood, Long> {
    List<Mood> findByUser(User user);
    List<Mood> findTop5ByUserOrderByCreatedAtDesc(User user);
}
