package com.calmora.DTO.journal;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JournalResponseDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
