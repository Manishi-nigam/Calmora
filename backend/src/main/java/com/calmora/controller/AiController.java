package com.calmora.controller;

import com.calmora.DTO.ai.AiRequestDTO;
import com.calmora.DTO.ai.AiResponseDTO;
import com.calmora.service.ConversationContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private ConversationContextService conversationContextService;

    @PostMapping
    public AiResponseDTO chat(@RequestBody AiRequestDTO request) {

        // Handle null or empty input
        if (request == null || request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return new AiResponseDTO("Please tell me how you're feeling. I'm here to help! 🤗");
        }

        String userMessage = request.getMessage().trim();

        try {
            System.out.println("[AI-DEBUG] AiController reached");
            
            // Try Gemini AI first using Context Service
            String aiReply = conversationContextService.processUserMessage(userMessage);
            
            if (aiReply != null && !aiReply.isBlank()) {
                System.out.println("[AI-DEBUG] FINAL RESPONSE SOURCE = GEMINI");
                // Check if Gemini flagged as irrelevant (keep this logic if you want)
                if (aiReply.trim().toUpperCase().contains("IRRELEVANT")) {
                    return new AiResponseDTO("I'm here to support your mental wellness. Let's talk about how you're feeling! 🌈");
                }
                return new AiResponseDTO(aiReply);
            }
        } catch (Exception e) {
            System.err.println("Error processing AI request: " + e.getMessage());
        }

        // Fallback to rule-based response
        System.out.println("[AI-DEBUG] FALLBACK getRuleBasedReply() CALLED");
        System.out.println("[AI-DEBUG] FINAL RESPONSE SOURCE = FALLBACK");
        return new AiResponseDTO(getRuleBasedReply(userMessage));
    }

    private String getRuleBasedReply(String userMessage) {
        System.out.println("[AI-DEBUG] !!! RULE BASED FALLBACK EXECUTED !!!");
        return "I'm having trouble connecting to Calmora AI right now. Please try again.";
    }
}
