package com.calmora.service;

import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.calmora.repository.ArticleRepository;
import com.calmora.repository.ShortVideoRepository; 
import com.calmora.repository.UserRepository;
import com.calmora.repository.MoodRepository;
import com.calmora.DTO.ArticleResponseDTO;
import com.calmora.DTO.RecommendationResponseDTO;
import com.calmora.DTO.ShortResponseDTO;
import com.calmora.model.Mood;
import com.calmora.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class RecommendationService {
    
    private final MoodRepository moodRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ShortVideoRepository shortVideoRepository;

    public RecommendationService(MoodRepository moodRepository,
         UserRepository userRepository,
          ArticleRepository articleRepository,
           ShortVideoRepository shortVideoRepository) {


        this.moodRepository = moodRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.shortVideoRepository = shortVideoRepository;
    }


    public RecommendationResponseDTO getRecommendations(){


        String email= SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getName();

        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));


        List<Mood> moods = moodRepository.findTop5ByUserOrderByCreatedAtDesc(user);

        if(moods.isEmpty()){
            return new RecommendationResponseDTO(
                "NONE","SELF_CARE", List.of(), List.of()
            );
        }

         Map<String, Long> frequencyMap =
                moods.stream()
                        .collect(Collectors.groupingBy(
                                Mood::getMoodType,
                                Collectors.counting()
                        ));

        String dominantMood = frequencyMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        String category = mapMoodToCategory(dominantMood);

        List<ArticleResponseDTO> articles =
                articleRepository.findByCategory(category)
                        .stream()
                        .map(article -> new ArticleResponseDTO(
                            article.getId(),
                            article.getTitle(),
                            article.getDescription(),
                            article.getImageUrl(),
                            article.getContent(),
                            article.getCategory(),
                            article.getCreatedAt(),
                            article.getAuthor(),
                            article.getKeyTakeaway()
                        )).toList();

        List<ShortResponseDTO> shorts =
                shortVideoRepository.findByCategory(category)
                        .stream()
                        .map(shortVideo -> new ShortResponseDTO(
                                shortVideo.getId(),
                                shortVideo.getTitle(),
                                shortVideo.getDescription(),
                                shortVideo.getThumbnailUrl(),
                                shortVideo.getVideoUrl(),
                                shortVideo.getDuration(),
                                shortVideo.getCategory()
                        ))
                        .toList();


        return new RecommendationResponseDTO(
                dominantMood,
                category,
                articles,
                shorts
        );
    }

    private String mapMoodToCategory(String mood) {

        return switch (mood.toUpperCase()) {

            case "STRESSED" -> "STRESS";

            case "ANXIOUS" -> "ANXIETY";

            case "SAD" -> "SELF_CARE";

            case "HAPPY" -> "MOTIVATION";

            case "CALM" -> "MINDFULNESS";

            default -> "SELF_CARE";
        };
    }
}
