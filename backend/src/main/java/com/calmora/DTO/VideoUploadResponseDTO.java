package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadResponseDTO {
    private String videoUrl;

    private String thumbnailUrl;

    private String publicId;

    private Integer duration;
}
