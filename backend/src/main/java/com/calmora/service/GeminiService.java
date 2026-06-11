package com.calmora.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    
    // Simple conversational history (keeps last 5 messages)
    private final List<String> chatHistory = new ArrayList<>();

    public GeminiService() {
        // Configure timeout: 5 seconds connect, 5 seconds read
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * Sends a message to Google Gemini API and returns the reply text.
     * Returns null if the API call fails (so caller can use fallback).
     */
    public String getReply(String userMessage) {
        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

            // Build conversation history string
            StringBuilder historyStr = new StringBuilder();
            if (!chatHistory.isEmpty()) {
                historyStr.append("Conversation:\n");
                for (String msg : chatHistory) {
                    historyStr.append(msg).append("\n");
                }
                historyStr.append("\n");
            }

            // Build combined system prompt and user message
            String systemPrompt = "You are a mental wellness assistant.\n"
                    + "You help users with stress, anxiety, sadness, and emotional support.\n"
                    + "Give short, helpful, and empathetic responses.\n"
                    + "Do not give unrelated or technical answers.\n"
                    + "Always respond in a calm and supportive tone.\n"
                    + "Limit your response to 2-3 meaningful sentences.\n"
                    + "If the user asks an unrelated or technical question, reply EXACTLY with 'IRRELEVANT'.\n\n"
                    + historyStr.toString()
                    + "User: " + userMessage + "\n"
                    + "Bot:";

            Map<String, Object> part = new HashMap<>();
            part.put("text", systemPrompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(part));

            Map<String, Object> body = new HashMap<>();
            body.put("contents", List.of(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // Call Gemini API (will timeout after 5 seconds)
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url, HttpMethod.POST, request,
                    (Class<Map<String, Object>>)(Class<?>)Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String text = extractText(response.getBody());
                if (text != null && !text.isBlank()) {
                    
                    // Don't save irrelevant fallback triggers to history
                    if (!text.trim().toUpperCase().contains("IRRELEVANT")) {
                        chatHistory.add("User: " + userMessage);
                        chatHistory.add("Bot: " + text);
                        
                        // Limit history to last 5 messages to keep it simple
                        while (chatHistory.size() > 5) {
                            chatHistory.remove(0);
                        }
                    }
                    return text;
                }
            }

            return null;

        } catch (org.springframework.web.client.ResourceAccessException e) {
            // Timeout or connection error
            System.err.println("Gemini API timeout: " + e.getMessage());
            return null;

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // 4xx errors (bad key, rate limit, etc.)
            System.err.println("Gemini API client error (" + e.getStatusCode() + "): " + e.getMessage());
            return null;

        } catch (org.springframework.web.client.HttpServerErrorException e) {
            // 5xx errors (Gemini server down)
            System.err.println("Gemini API server error (" + e.getStatusCode() + "): " + e.getMessage());
            return null;

        } catch (Exception e) {
            // Any other unexpected error
            System.err.println("Gemini API unexpected error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts text from Gemini API response.
     * Structure: { "candidates": [{ "content": { "parts": [{ "text": "..." }] } }] }
     */
    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> candidates =
                (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates == null || candidates.isEmpty()) return null;

            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> contentMap =
                (Map<String, Object>) firstCandidate.get("content");
            if (contentMap == null) return null;

            List<Map<String, Object>> parts =
                (List<Map<String, Object>>) contentMap.get("parts");
            if (parts == null || parts.isEmpty()) return null;

            return (String) parts.get(0).get("text");

        } catch (Exception e) {
            System.err.println("Error parsing Gemini response: " + e.getMessage());
            return null;
        }
    }
}
