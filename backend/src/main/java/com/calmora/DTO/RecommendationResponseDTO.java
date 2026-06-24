package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponseDTO {
    
    private String dominantMood;

    private String category;

    private List<ArticleResponseDTO> recommendedArticles;
    
    private List<ShortResponseDTO> recommendedShorts;
}
