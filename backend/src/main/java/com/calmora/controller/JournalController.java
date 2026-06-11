package com.calmora.controller;

import com.calmora.DTO.journal.JournalRequestDTO;
import com.calmora.DTO.journal.JournalResponseDTO;
import com.calmora.model.JournalEntry;
import com.calmora.service.JournalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    private final JournalService journalService;

    // Constructor injection
    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }


    @PostMapping
    public JournalResponseDTO addJournal(@RequestBody JournalRequestDTO entry) {
        return journalService.addJournal(entry);
    }


    @GetMapping
    public List<JournalResponseDTO> getAllJournal() {
        return journalService.getAllJournals();
    }
}
