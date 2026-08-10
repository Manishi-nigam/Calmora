package com.calmora.service;

import com.calmora.model.AiMessage;
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

    @Value("${gemini.model:gemini-1.5-flash-latest}")
    private String configuredModel;

    private final RestTemplate restTemplate;

    public GeminiService() {
        // Configure timeout: 5 seconds connect, 10 seconds read
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        this.restTemplate = new RestTemplate(factory);
    }

    @jakarta.annotation.PostConstruct
    public void listAvailableModels() {
        try {
            if (apiKey == null || apiKey.isBlank()) {
                System.out.println("[AI-DEBUG] GEMINI_API_KEY is blank. Skipping model list.");
                return;
            }
            
            System.out.println("\n[AI-DEBUG] FETCHING AVAILABLE GEMINI MODELS...");
            String url = "https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey;
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                System.out.println("[AI-DEBUG] AVAILABLE GEMINI MODELS:");
                List<Map<String, Object>> models = (List<Map<String, Object>>) response.getBody().get("models");
                for (Map<String, Object> model : models) {
                    String name = (String) model.get("name");
                    List<String> methods = (List<String>) model.get("supportedGenerationMethods");
                    System.out.println("  - MODEL: " + name);
                    System.out.println("    SUPPORTED METHODS: " + methods);
                }
            }
        } catch (Exception e) {
            System.err.println("[AI-DEBUG] Failed to list Gemini models: " + e.getMessage());
        }
        System.out.println("\n[AI-DEBUG] CONFIGURATION: Using model = " + configuredModel + "\n");
    }

    /**
     * Sends a structured prompt to Google Gemini API and returns the reply text.
     */
    public String getReply(String systemInstructionText, List<AiMessage> history, String currentUserMessageText) {
        try {
            System.out.println("[AI-DEBUG] GeminiService reached");
            System.out.println("[AI-DEBUG] Gemini API key configured = " + (apiKey != null && !apiKey.isBlank()));
            System.out.println("[AI-DEBUG] GEMINI_API_KEY length = " + (apiKey != null ? apiKey.length() : 0));
            System.out.println("[AI-DEBUG] GEMINI MODEL = " + configuredModel);
            System.out.println("[AI-DEBUG] GEMINI API VERSION = v1beta");
            
            // Using a stable supported model for conversational flows
            String url = "https://generativelanguage.googleapis.com/v1beta/models/" + configuredModel + ":generateContent?key=" + apiKey;
            System.out.println("[AI-DEBUG] GEMINI ENDPOINT = " + url.replace(apiKey, "REDACTED_KEY"));

            // 1. System Instruction Node
            Map<String, Object> systemInstruction = new HashMap<>();
            systemInstruction.put("parts", List.of(Map.of("text", systemInstructionText)));

            // 2. Contents Array (History + Current Message)
            List<Map<String, Object>> contents = new ArrayList<>();

            if (history != null) {
                for (AiMessage msg : history) {
                    Map<String, Object> content = new HashMap<>();
                    String role = msg.getRole().equalsIgnoreCase("USER") ? "user" : "model";
                    content.put("role", role);
                    content.put("parts", List.of(Map.of("text", msg.getContent())));
                    contents.add(content);
                }
            }

            // Current message
            Map<String, Object> currentContent = new HashMap<>();
            currentContent.put("role", "user");
            currentContent.put("parts", List.of(Map.of("text", currentUserMessageText)));
            contents.add(currentContent);

            // 3. Generation Config
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.2); // Low temperature for consistency
            generationConfig.put("topK", 40);
            generationConfig.put("topP", 0.95);

            // 4. Assemble Request Body
            Map<String, Object> body = new HashMap<>();
            body.put("systemInstruction", systemInstruction);
            body.put("contents", contents);
            body.put("generationConfig", generationConfig);
            
            System.out.println("[AI-DEBUG] CALLING GEMINI");
            System.out.println("[AI-DEBUG] MODEL = " + configuredModel);
            System.out.println("[AI-DEBUG] API URL = https://generativelanguage.googleapis.com/v1beta/models/" + configuredModel + ":generateContent");
            System.out.println("[AI-DEBUG] REQUEST BODY = " + body);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url, HttpMethod.POST, request,
                    (Class<Map<String, Object>>)(Class<?>)Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                System.out.println("[AI-DEBUG] GEMINI RESPONSE RECEIVED");
                String text = extractText(response.getBody());
                if (text != null && !text.isBlank()) {
                    return text.trim();
                }
            }

            throw new RuntimeException("Gemini returned OK but body was null or empty");

        } catch (org.springframework.web.client.ResourceAccessException e) {
            System.err.println("Gemini API timeout: " + e.getMessage());
            return null;

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.err.println("[AI-DEBUG] GEMINI ERROR");
            System.err.println("[AI-DEBUG] Gemini exception class: " + e.getClass().getName());
            System.err.println("[AI-DEBUG] Gemini exception message: " + e.getMessage());
            System.err.println("[AI-DEBUG] HTTP status: " + e.getStatusCode());
            System.err.println("[AI-DEBUG] Response body: " + e.getResponseBodyAsString());
            throw new RuntimeException("Gemini API client error", e);

        } catch (org.springframework.web.client.HttpServerErrorException e) {
            System.err.println("[AI-DEBUG] GEMINI ERROR");
            System.err.println("[AI-DEBUG] Gemini exception class: " + e.getClass().getName());
            System.err.println("[AI-DEBUG] Gemini exception message: " + e.getMessage());
            System.err.println("[AI-DEBUG] HTTP status: " + e.getStatusCode());
            System.err.println("[AI-DEBUG] Response body: " + e.getResponseBodyAsString());
            throw new RuntimeException("Gemini API server error", e);

        } catch (Exception e) {
            System.err.println("[AI-DEBUG] GEMINI ERROR");
            System.err.println("[AI-DEBUG] Gemini exception class: " + e.getClass().getName());
            System.err.println("[AI-DEBUG] Gemini exception message: " + e.getMessage());
            throw new RuntimeException("Gemini API unexpected error", e);
        }
    }

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
