package com.calmora.service;

import com.calmora.DTO.ArticleResponseDTO;
import com.calmora.DTO.SavedResponseDTO;
import com.calmora.DTO.ShortResponseDTO;
import com.calmora.model.*;
import com.calmora.repository.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SavedService {

    private final SavedArticleRepository savedArticleRepository;
    private final SavedShortRepository savedShortRepository;
    private final ArticleRepository articleRepository;
    private final ShortVideoRepository shortVideoRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ---------------- SAVE ARTICLE ----------------

    public String saveArticle(Long articleId) {

        User user = getCurrentUser();

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        if (savedArticleRepository.findByUserAndArticle(user, article).isPresent()) {
            return "Article already saved";
        }

        SavedArticle savedArticle = new SavedArticle();
        savedArticle.setUser(user);
        savedArticle.setArticle(article);
        savedArticle.setSavedAt(LocalDateTime.now());

        savedArticleRepository.save(savedArticle);

        return "Article saved successfully";
    }

    // ---------------- SAVE SHORT ----------------

    public String saveShort(Long shortId) {

        User user = getCurrentUser();

        ShortVideo shortVideo = shortVideoRepository.findById(shortId)
                .orElseThrow(() -> new RuntimeException("Short not found"));

        if (savedShortRepository.findByUserAndShortVideo(user, shortVideo).isPresent()) {
            return "Short already saved";
        }

        SavedShort savedShort = new SavedShort();
        savedShort.setUser(user);
        savedShort.setShortVideo(shortVideo);
        savedShort.setSavedAt(LocalDateTime.now());

        savedShortRepository.save(savedShort);

        return "Short saved successfully";
    }

    // ---------------- REMOVE ARTICLE ----------------

    public String removeSavedArticle(Long articleId) {

        User user = getCurrentUser();

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        SavedArticle savedArticle = savedArticleRepository
                .findByUserAndArticle(user, article)
                .orElseThrow(() -> new RuntimeException("Article not saved"));

        savedArticleRepository.delete(savedArticle);

        return "Article removed successfully";
    }

    // ---------------- REMOVE SHORT ----------------

    public String removeSavedShort(Long shortId) {

        User user = getCurrentUser();

        ShortVideo shortVideo = shortVideoRepository.findById(shortId)
                .orElseThrow(() -> new RuntimeException("Short not found"));

        SavedShort savedShort = savedShortRepository
                .findByUserAndShortVideo(user, shortVideo)
                .orElseThrow(() -> new RuntimeException("Short not saved"));

        savedShortRepository.delete(savedShort);

        return "Short removed successfully";
    }

    // ---------------- GET SAVED ARTICLES ----------------

    public List<ArticleResponseDTO> getSavedArticles() {

        User user = getCurrentUser();

        return savedArticleRepository.findByUser(user)
                .stream()
                .map(saved -> {

                    Article article = saved.getArticle();

                    return new ArticleResponseDTO(
                            article.getId(),
                            article.getTitle(),
                            article.getDescription(),
                            article.getImageUrl(),
                            article.getContent(),
                            article.getCategory(),
                            article.getCreatedAt(),
                            article.getAuthor(),
                            article.getKeyTakeaway()
                    );
                })
                .toList();
    }

    // ---------------- GET SAVED SHORTS ----------------

    public List<ShortResponseDTO> getSavedShorts() {

        User user = getCurrentUser();

        return savedShortRepository.findByUser(user)
                .stream()
                .map(saved -> {

                    ShortVideo shortVideo = saved.getShortVideo();

                    return new ShortResponseDTO(
                            shortVideo.getId(),
                            shortVideo.getTitle(),
                            shortVideo.getDescription(),
                            shortVideo.getThumbnailUrl(),
                            shortVideo.getVideoUrl(),
                            shortVideo.getDuration(),
                            shortVideo.getCategory()
                    );
                })
                .toList();
    }

    // ---------------- GET ALL SAVED ----------------

    public SavedResponseDTO getAllSaved() {

        return new SavedResponseDTO(
                getSavedArticles(),
                getSavedShorts()
        );
    }
}
