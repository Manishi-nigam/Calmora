package com.calmora.service;

import com.calmora.DTO.ArticleRequestDTO;
import com.calmora.DTO.ArticleResponseDTO;
import com.calmora.model.Article;
import com.calmora.model.User;
import com.calmora.repository.ArticleRepository;
import com.calmora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private UserRepository UserRepository;
    public ArticleRepository articleRepository;

    ArticleService(ArticleRepository articleRepository){
        this.articleRepository= articleRepository;
    }
    public ArticleResponseDTO createArticle(ArticleRequestDTO req) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        User user = UserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article article = new Article();

        article.setTitle(req.getTitle());
        article.setDescription(req.getDescription());
        article.setImageUrl(req.getImageUrl());
        article.setContent(req.getContent());
        article.setCategory(req.getCategory());
        article.setCreatedAt(LocalDateTime.now());

        Article savedArticle = articleRepository.save(article);

        return new ArticleResponseDTO(
                savedArticle.getId(),
                savedArticle.getTitle(),
                savedArticle.getDescription(),
                savedArticle.getImageUrl(),
                savedArticle.getContent(),
                savedArticle.getCategory(),
                savedArticle.getCreatedAt()
        );
    }

    public List<ArticleResponseDTO> getAllArticles() {

        List<Article> articles = articleRepository.findAll();

        return articles.stream()
                .map(article -> new ArticleResponseDTO(
                        article.getId(),
                        article.getTitle(),
                        article.getDescription(),
                        article.getImageUrl(),
                        article.getContent(),
                        article.getCategory(),
                        article.getCreatedAt()
                ))
                .toList();
    }

    public ArticleResponseDTO getArticleById(Long id) {

        Article article = articleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Article not found"));

        return new ArticleResponseDTO(
                article.getId(),
                article.getTitle(),
                article.getDescription(),
                article.getImageUrl(),
                article.getContent(),
                article.getCategory(),
                article.getCreatedAt()
        );
    }
}
