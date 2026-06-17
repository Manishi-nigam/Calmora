package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestDTO {
    public String title;

    public String description;

    public String imageUrl;

    public String content;

    public String category;

    public String author;

    public String keyTakeaway;
}
