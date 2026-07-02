package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortRequestDTO {

    public String title;
    public String description;
    public String category;
}
