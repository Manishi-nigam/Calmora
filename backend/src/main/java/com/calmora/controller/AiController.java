package com.calmora.controller;

import com.calmora.DTO.ai.AiRequestDTO;
import com.calmora.DTO.ai.AiResponseDTO;
import com.calmora.model.AiRequest;
import com.calmora.model.AiResponse;
import com.calmora.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private GeminiService geminiService;


    @PostMapping
    public AiResponseDTO chat(@RequestBody AiRequestDTO request) {

        // Handle null or empty input
        if (request == null || request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return new AiResponseDTO("Please tell me how you're feeling. I'm here to help! 🤗");
        }

        String userMessage = request.getMessage().trim();

        // Try Gemini AI first
        String geminiReply = geminiService.getReply(userMessage);

        if (geminiReply != null && !geminiReply.isBlank()) {
            // Check if Gemini flagged as irrelevant
            if (geminiReply.trim().toUpperCase().contains("IRRELEVANT")) {
                return new AiResponseDTO("I'm here to support your mental wellness. Let's talk about how you're feeling! 🌈");
            }
            return new AiResponseDTO(geminiReply);
        }

        // Fallback to rule-based response
        return new AiResponseDTO(getRuleBasedReply(userMessage));
    }

    private String getRuleBasedReply(String userMessage) {
        String msg = userMessage.toLowerCase();

        if (msg.contains("stress") || msg.contains("pressure")) {
            return "Try deep breathing for 2 minutes. Close your eyes and count to 5 slowly. You're doing your best! 💪";

        } else if (msg.contains("sad") || msg.contains("unhappy") || msg.contains("depressed")) {
            return "It's okay to feel sad sometimes. Try writing down 3 things you're grateful for. I'm here for you. 💙";

        } else if (msg.contains("anxious") || msg.contains("anxiety") || msg.contains("worried")) {
            return "Try the 5-4-3-2-1 grounding technique: notice 5 things you see, 4 you hear, 3 you touch, 2 you smell, 1 you taste. 🌿";

        } else if (msg.contains("angry") || msg.contains("frustrated")) {
            return "Take a moment to breathe deeply. Count to 10 before reacting. A short walk can help too. 🧘";

        } else if (msg.contains("tired") || msg.contains("exhausted") || msg.contains("sleep")) {
            return "Rest is important. Try calming music or dim your lights. A 20-minute nap can help! 😴";

        } else if (msg.contains("happy") || msg.contains("good") || msg.contains("great")) {
            return "That's wonderful! Keep doing what makes you feel good. Celebrate the small wins! 🎉";

        } else if (msg.contains("lonely") || msg.contains("alone")) {
            return "You are valued and loved. Try reaching out to someone you trust. I'm always here! 🤝";

        } else if (msg.contains("hello") || msg.contains("hi") || msg.contains("hey")) {
            return "Hello! 👋 Welcome to Calmora. Tell me how you're feeling today!";

        } else if (msg.contains("thank")) {
            return "You're welcome! Taking care of your mental health is a sign of strength. 💛";

        } else {
            return "I'm here to support you. Tell me more about how you're feeling. 🌈";
        }
    }
}
