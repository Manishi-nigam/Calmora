package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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

    private List<String> qualifications;

    private List<String> specializations;
}
