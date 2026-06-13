package com.calmora.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDTO {


   public Long id;

    public String title;

    public String description;

    public String imageUrl;

    public String content;

    public String category;

    public LocalDateTime createdAt;

}
