package com.calmora.repository;

import com.calmora.model.Article;
import com.calmora.model.SavedArticle;
import com.calmora.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {

    List<SavedArticle> findByUser(User user);

    Optional<SavedArticle> findByUserAndArticle(User user, Article article);
}
