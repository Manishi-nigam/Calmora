package com.calmora.service;

import com.calmora.model.AiMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PromptBuilderTest {

    private PromptBuilder promptBuilder;

    @BeforeEach
    void setUp() {
        promptBuilder = new PromptBuilder();
    }

    @Test
    void testBuildMentalHealthPrompt_IncludesAllSections() {
        List<AiMessage> recentMessages = new ArrayList<>();
        AiMessage m1 = new AiMessage();
        m1.setRole("USER");
        m1.setContent("I am stressed.");
        
        AiMessage m2 = new AiMessage();
        m2.setRole("ASSISTANT");
        m2.setContent("Why are you stressed?");
        
        recentMessages.add(m1);
        recentMessages.add(m2);

        String summary = "User is experiencing stress.";
        String currentMessage = "Because of work.";

        String prompt = promptBuilder.buildMentalHealthPrompt(recentMessages, summary, currentMessage);

        assertTrue(prompt.contains("<system_instructions>"), "Must contain system instructions tag");
        assertTrue(prompt.contains("You are Calmora"), "Must contain bot persona");
        
        assertTrue(prompt.contains("<conversation_summary>"), "Must contain summary tag");
        assertTrue(prompt.contains(summary), "Must contain summary text");

        assertTrue(prompt.contains("<conversation_history>"), "Must contain history tag");
        assertTrue(prompt.contains("USER: I am stressed."), "Must contain history user message");
        assertTrue(prompt.contains("ASSISTANT: Why are you stressed?"), "Must contain history assistant message");

        assertTrue(prompt.contains("<response_instructions>"), "Must contain response instructions tag");
        
        assertTrue(prompt.contains("<current_user_message>"), "Must contain current message tag");
        assertTrue(prompt.contains(currentMessage), "Must contain current message text");

        assertTrue(prompt.contains("ASSISTANT:\n"), "Must end with ASSISTANT:");
    }

    @Test
    void testBuildMentalHealthPrompt_WithoutSummary() {
        List<AiMessage> recentMessages = new ArrayList<>();
        String currentMessage = "Hello!";

        String prompt = promptBuilder.buildMentalHealthPrompt(recentMessages, null, currentMessage);

        assertTrue(!prompt.contains("<conversation_summary>"), "Should not contain summary tag when null");
        assertTrue(!prompt.contains("<conversation_history>"), "Should not contain history tag when empty");
        assertTrue(prompt.contains("<current_user_message>"), "Must contain current message tag");
        assertTrue(prompt.contains(currentMessage), "Must contain current message text");
    }
}
