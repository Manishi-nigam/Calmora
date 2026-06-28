package com.calmora.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequestDTO {
    private String username;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
    private String profileImageUrl;

    
}
