package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TherapistListDTO {

    private Long id;

    private String name;

    private String specialization;

    private Integer experience;

    private String imageUrl;

    private List<String> qualifications;
    
    private List<String> specializations;
}