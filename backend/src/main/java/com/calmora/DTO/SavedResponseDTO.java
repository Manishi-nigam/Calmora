package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedResponseDTO {

    private List<ArticleResponseDTO> articles;

    private List<ShortResponseDTO> shorts;
}
