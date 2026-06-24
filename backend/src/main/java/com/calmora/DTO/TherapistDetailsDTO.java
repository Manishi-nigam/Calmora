package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TherapistDetailsDTO {

    private Long id;

    private String name;

    private String specialization;

    private Integer experience;

    private String imageUrl;

    private String bio;

    private String email;

    private Integer clientsServed;
    
    private String consultationFee;
}
