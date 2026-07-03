package com.calmora.controller;

import com.calmora.DTO.ArticleResponseDTO;
import com.calmora.DTO.SavedResponseDTO;
import com.calmora.DTO.ShortResponseDTO;
import com.calmora.service.SavedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/saved")
@RequiredArgsConstructor
public class SavedController {

    private final SavedService savedService;

    // Save Article
    @PostMapping("/articles/{articleId}")
    public ResponseEntity<String> saveArticle(
            @PathVariable Long articleId) {

        return ResponseEntity.ok(
                savedService.saveArticle(articleId)
        );
    }

    // Save Short
    @PostMapping("/shorts/{shortId}")
    public ResponseEntity<String> saveShort(
            @PathVariable Long shortId) {

        return ResponseEntity.ok(
                savedService.saveShort(shortId)
        );
    }

    // Remove Saved Article
    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<String> removeSavedArticle(
            @PathVariable Long articleId) {

        return ResponseEntity.ok(
                savedService.removeSavedArticle(articleId)
        );
    }

    // Remove Saved Short
    @DeleteMapping("/shorts/{shortId}")
    public ResponseEntity<String> removeSavedShort(
            @PathVariable Long shortId) {

        return ResponseEntity.ok(
                savedService.removeSavedShort(shortId)
        );
    }

    // Get All Saved Content
    @GetMapping
    public ResponseEntity<SavedResponseDTO> getAllSaved() {

        return ResponseEntity.ok(
                savedService.getAllSaved()
        );
    }

    // Get Saved Articles
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponseDTO>> getSavedArticles() {

        return ResponseEntity.ok(
                savedService.getSavedArticles()
        );
    }

    // Get Saved Shorts
    @GetMapping("/shorts")
    public ResponseEntity<List<ShortResponseDTO>> getSavedShorts() {

        return ResponseEntity.ok(
                savedService.getSavedShorts()
        );
    }
}
