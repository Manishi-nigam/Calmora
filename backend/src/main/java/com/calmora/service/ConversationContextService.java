package com.calmora.service;

import com.calmora.model.AiConversation;
import com.calmora.model.AiMessage;
import com.calmora.model.User;
import com.calmora.repository.AiConversationRepository;
import com.calmora.repository.AiMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationContextService {

    @Autowired
    private AiConversationRepository conversationRepository;

    @Autowired
    private AiMessageRepository messageRepository;

    @Autowired
    private PromptBuilder promptBuilder;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private com.calmora.repository.UserRepository userRepository;

    // Keep last 10 messages (5 turns) in context
    private static final int MAX_CONTEXT_MESSAGES = 10;

    public String processUserMessage(String userMessageText) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Get or create active conversation
        AiConversation conversation = conversationRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .orElseGet(() -> {
                    AiConversation newConv = new AiConversation();
                    newConv.setUser(user);
                    return conversationRepository.save(newConv);
                });

        // 2. Fetch history
        List<AiMessage> allMessages = messageRepository.findByConversationOrderByCreatedAtAsc(conversation);

        // 3. Select recent messages and generate summary if needed
        List<AiMessage> recentMessages;
        String conversationSummary = null;

        if (allMessages.size() > MAX_CONTEXT_MESSAGES) {
            recentMessages = allMessages.subList(allMessages.size() - MAX_CONTEXT_MESSAGES, allMessages.size());
            conversationSummary = "The user has been having a continuous conversation. Previous context might be omitted for brevity.";
            // In a fully developed implementation, we would call an LLM here to summarize:
            // allMessages.subList(0, allMessages.size() - MAX_CONTEXT_MESSAGES)
        } else {
            recentMessages = allMessages;
        }

        // 4. Build Prompt
        String prompt = promptBuilder.buildMentalHealthPrompt(recentMessages, conversationSummary, userMessageText);

        System.out.println("--- Prompt Built for User " + user.getId() + " ---");
        System.out.println("Prompt length: " + prompt.length() + " chars");
        System.out.println("History included: " + recentMessages.size() + " messages");

        // 5. Call LLM
        String aiResponseText = geminiService.getReply(prompt);

        if (aiResponseText == null || aiResponseText.isBlank()) {
            return null; // Let controller handle fallback
        }

        // 6. Save the new messages to DB
        AiMessage userMsg = new AiMessage();
        userMsg.setConversation(conversation);
        userMsg.setRole("USER");
        userMsg.setContent(userMessageText);
        messageRepository.save(userMsg);

        AiMessage assistantMsg = new AiMessage();
        assistantMsg.setConversation(conversation);
        assistantMsg.setRole("ASSISTANT");
        assistantMsg.setContent(aiResponseText);
        messageRepository.save(assistantMsg);

        return aiResponseText;
    }
}
