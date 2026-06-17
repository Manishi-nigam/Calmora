package com.calmora.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WisdomRequestDTO {
    private String quote;
    private String author;
    
}
