package com.calmora.controller;

import com.calmora.DTO.ArticleRequestDTO;
import com.calmora.DTO.ArticleResponseDTO;
import com.calmora.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> createArticle(
            @RequestBody ArticleRequestDTO request) {

        return ResponseEntity.ok(
                articleService.createArticle(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {

        return ResponseEntity.ok(
                articleService.getAllArticles()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> getArticleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                articleService.getArticleById(id)
        );
    }
}
