package com.calmora.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortResponseDTO {

    public Long id;
    public String title;
    public String description;
    public String thumbnailUrl;
    public String videoUrl;
    public Integer duration;
    public String category;
}
