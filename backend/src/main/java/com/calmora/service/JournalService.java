package com.calmora.service;

import com.calmora.DTO.journal.JournalRequestDTO;
import com.calmora.DTO.journal.JournalResponseDTO;
import com.calmora.model.JournalEntry;
import com.calmora.model.User;
import com.calmora.repository.JournalRepository;
import com.calmora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private UserRepository UserRepository;

    public JournalResponseDTO addJournal(
            JournalRequestDTO request
    ) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        User user = UserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JournalEntry journal = new JournalEntry();

        journal.setTitle(request.getTitle());
        journal.setContent(request.getContent());
        journal.setCreatedAt(LocalDateTime.now());

        JournalEntry savedJournal =
                journalRepository.save(journal);

        return new JournalResponseDTO(
                savedJournal.getId(),
                savedJournal.getTitle(),
                savedJournal.getContent(),
                savedJournal.getCreatedAt()
        );
    }

    public List<JournalResponseDTO> getAllJournals() {

        return journalRepository.findAll()
                .stream()
                .map(journal -> new JournalResponseDTO(
                        journal.getId(),
                        journal.getTitle(),
                        journal.getContent(),
                        journal.getCreatedAt()
                ))
                .toList();
    }
}