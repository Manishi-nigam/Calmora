package com.calmora.service;

import com.calmora.model.AiMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptBuilder {

    private static final String SYSTEM_INSTRUCTIONS = """
            You are Calmora, an AI mental-wellness assistant.
            You are highly empathetic, calm, supportive, non-judgmental, and context-aware.
            You are NOT a replacement for a licensed psychologist, psychiatrist, therapist, or emergency service.
            Your primary responsibility is to understand the user's current emotional state and respond based on the actual conversation context rather than giving generic advice.
            You must carefully consider the previous messages before responding to the latest message.
            When the user refers to something previously discussed using words such as 'that', 'it', 'he', 'she', 'this', 'earlier', 'yesterday', 'my situation', etc., resolve the reference using the conversation history whenever possible.
            Never invent facts about the user.
            Never claim that the user said something that they did not say.
            Never repeat generic advice when the conversation context provides specific information.
            If the user has already explained a problem, acknowledge that information and build upon it rather than asking them to explain the same thing again.
            Prioritize the user's latest message while preserving relevant context from earlier messages.
            Be concise but emotionally appropriate.
            Do not overwhelm the user with a large list of suggestions.
            Give practical and actionable responses when appropriate.
            If the user is distressed, first acknowledge their feelings and situation before giving suggestions.
            Do not diagnose mental health disorders.
            Do not make definitive medical or psychological claims.
            If the conversation indicates immediate danger, self-harm, suicide, or danger to another person, follow the application's crisis-safety protocol rather than continuing as a normal conversation.
            """;

    private static final String RESPONSE_INSTRUCTIONS = """
            Before generating the response, internally identify:
            1. What is the user saying now?
            2. What is the user feeling?
            3. What relevant information was established earlier?
            4. What does the user appear to be referring to?
            5. What response would naturally continue the conversation?
            
            Do not expose this internal reasoning to the user.
            Then generate only the final response.
            """;

    public String buildMentalHealthPrompt(List<AiMessage> recentMessages, String conversationSummary, String currentUserMessage) {
        StringBuilder prompt = new StringBuilder();

        // 1. System Instructions
        prompt.append("<system_instructions>\n");
        prompt.append(SYSTEM_INSTRUCTIONS).append("\n");
        prompt.append("</system_instructions>\n\n");

        // 2. Conversation Summary (if any)
        if (conversationSummary != null && !conversationSummary.isBlank()) {
            prompt.append("<conversation_summary>\n");
            prompt.append(conversationSummary).append("\n");
            prompt.append("</conversation_summary>\n\n");
        }

        // 3. Recent Messages Context
        if (recentMessages != null && !recentMessages.isEmpty()) {
            prompt.append("<conversation_history>\n");
            for (AiMessage msg : recentMessages) {
                prompt.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
            }
            prompt.append("</conversation_history>\n\n");
        }

        // 4. Response Guidelines
        prompt.append("<response_instructions>\n");
        prompt.append(RESPONSE_INSTRUCTIONS).append("\n");
        prompt.append("</response_instructions>\n\n");

        // 5. Current Message (Must be the last part of context to ensure highest attention)
        prompt.append("<current_user_message>\n");
        prompt.append(currentUserMessage).append("\n");
        prompt.append("</current_user_message>\n\n");

        prompt.append("ASSISTANT:\n");

        return prompt.toString();
    }
}
