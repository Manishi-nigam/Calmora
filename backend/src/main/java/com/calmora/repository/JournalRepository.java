package com.calmora.repository;

import com.calmora.model.JournalEntry;
import com.calmora.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<JournalEntry, Long> {
 List<JournalEntry> findByUser(User user);
}
