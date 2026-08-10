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

    public GeminiService() {
        // Configure timeout: 5 seconds connect, 10 seconds read (since prompts might be longer)
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * Sends a raw prompt to Google Gemini API and returns the reply text.
     */
    public String getReply(String prompt) {
        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(part));

            // Add generation config for consistency
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.2); // Low temperature for consistency
            generationConfig.put("topK", 40);
            generationConfig.put("topP", 0.95);

            // Add safety settings (optional, but good practice for mental health)
            List<Map<String, Object>> safetySettings = new ArrayList<>();
            // We can configure safety settings if needed, but defaults are usually fine

            Map<String, Object> body = new HashMap<>();
            body.put("contents", List.of(content));
            body.put("generationConfig", generationConfig);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url, HttpMethod.POST, request,
                    (Class<Map<String, Object>>)(Class<?>)Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String text = extractText(response.getBody());
                if (text != null && !text.isBlank()) {
                    return text.trim();
                }
            }

            return null;

        } catch (org.springframework.web.client.ResourceAccessException e) {
            System.err.println("Gemini API timeout: " + e.getMessage());
            return null;

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.err.println("Gemini API client error (" + e.getStatusCode() + "): " + e.getMessage());
            return null;

        } catch (org.springframework.web.client.HttpServerErrorException e) {
            System.err.println("Gemini API server error (" + e.getStatusCode() + "): " + e.getMessage());
            return null;

        } catch (Exception e) {
            System.err.println("Gemini API unexpected error: " + e.getMessage());
            return null;
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
