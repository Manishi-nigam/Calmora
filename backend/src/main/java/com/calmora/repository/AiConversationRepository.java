package com.calmora.repository;

import com.calmora.model.AiConversation;
import com.calmora.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiConversationRepository extends JpaRepository<AiConversation, Long> {
    Optional<AiConversation> findFirstByUserOrderByCreatedAtDesc(User user);
}
