package com.calmora.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PromptBuilderTest {

    private PromptBuilder promptBuilder;

    @BeforeEach
    void setUp() {
        promptBuilder = new PromptBuilder();
    }

    @Test
    void testGetSystemInstructions_Default() {
        String prompt = promptBuilder.getSystemInstructions(false);

        assertTrue(prompt.contains("You are Calmora"), "Must contain bot persona");
        assertTrue(prompt.contains("IMPORTANT RULES ABOUT CONTEXT AND TONE"), "Must contain rules");
        assertFalse(prompt.contains("CRITICAL CONVERSATIONAL PREFERENCE ACTIVE"), "Should not contain preference override");
    }

    @Test
    void testGetSystemInstructions_WithAdviceDisabled() {
        String prompt = promptBuilder.getSystemInstructions(true);

        assertTrue(prompt.contains("You are Calmora"), "Must contain bot persona");
        assertTrue(prompt.contains("CRITICAL CONVERSATIONAL PREFERENCE ACTIVE"), "Must contain preference override");
        assertTrue(prompt.contains("DO NOT suggest breathing"), "Must instruct not to suggest remedies");
    }
}
