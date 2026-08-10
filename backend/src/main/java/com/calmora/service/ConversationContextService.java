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
        List<AiMessage> recentMessages;

        if (allMessages.size() > MAX_CONTEXT_MESSAGES) {
            recentMessages = allMessages.subList(allMessages.size() - MAX_CONTEXT_MESSAGES, allMessages.size());
        } else {
            recentMessages = allMessages;
        }

        // 3. Extract Conversational Preferences
        boolean disableAdvice = checkAdvicePreference(recentMessages, userMessageText);
        
        // 4. Get System Instructions
        String systemInstruction = promptBuilder.getSystemInstructions(disableAdvice);

        // ==========================================
        // DEVELOPMENT LOGGING
        // ==========================================
        System.out.println("\n=== AI REQUEST ===");
        System.out.println("userId: " + user.getId());
        System.out.println("conversationKey: " + conversation.getId());
        System.out.println("historySize: " + recentMessages.size());
        System.out.println("disableAdvice: " + disableAdvice);
        System.out.println("CURRENT MESSAGE: " + userMessageText);
        // ==========================================

        // 5. Call LLM natively
        String aiResponseText = geminiService.getReply(systemInstruction, recentMessages, userMessageText);

        // ==========================================
        // RESPONSE VALIDATION & RETRY (LOOP PREVENTION)
        // ==========================================
        if (aiResponseText != null && !aiResponseText.isBlank()) {
            boolean isUserNegative = userMessageText.toLowerCase().contains("not") 
                    || userMessageText.toLowerCase().contains("bad") 
                    || userMessageText.toLowerCase().contains("irritated")
                    || userMessageText.toLowerCase().contains("angry")
                    || userMessageText.toLowerCase().contains("sad")
                    || userMessageText.toLowerCase().contains("frustrated");
                    
            boolean isResponseOverlyPositive = aiResponseText.toLowerCase().contains("that's wonderful") 
                    || aiResponseText.toLowerCase().contains("celebrate")
                    || aiResponseText.toLowerCase().contains("that's great")
                    || aiResponseText.toLowerCase().contains("keep doing what makes you feel good");
                    
            boolean isResponseLooping = false;
            if (!recentMessages.isEmpty()) {
                String lastAiMsg = recentMessages.get(recentMessages.size() - 1).getContent();
                if (recentMessages.get(recentMessages.size() - 1).getRole().equals("ASSISTANT") && 
                    aiResponseText.equals(lastAiMsg)) {
                    isResponseLooping = true;
                }
            }

            if ((isUserNegative && isResponseOverlyPositive) || isResponseLooping || aiResponseText.toLowerCase().contains("could you tell me a little more")) {
                System.out.println("WARNING: Context validation failed! Regenerating response...");
                
                String correctionInstruction = systemInstruction + "\n\n" + 
                        "SYSTEM CORRECTION: The previous response you generated was either a repetitive loop, overly positive, or a generic 'tell me more'. " +
                        "Generate a completely new response that directly acknowledges the user's stated emotion, DOES NOT repeat yourself, DOES NOT ask them to repeat themselves, and DOES NOT force positivity.";
                
                aiResponseText = geminiService.getReply(correctionInstruction, recentMessages, userMessageText);
            }
        }

        System.out.println("\n=== AI RESPONSE ===\n" + aiResponseText + "\n===================");

        if (aiResponseText == null || aiResponseText.isBlank()) {
            return null; // Let controller handle fallback
        }

        // 6. Save BOTH messages to DB immediately to preserve sequence
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
    
    private boolean checkAdvicePreference(List<AiMessage> history, String currentMessage) {
        String combined = currentMessage.toLowerCase();
        // check history for recent preference
        for (int i = Math.max(0, history.size() - 4); i < history.size(); i++) {
            if (history.get(i).getRole().equals("USER")) {
                combined += " " + history.get(i).getContent().toLowerCase();
            }
        }
        
        return combined.contains("don't suggest") || 
               combined.contains("no remedies") || 
               combined.contains("just talk") ||
               combined.contains("talk normally") ||
               combined.contains("don't give me advice");
    }
}
