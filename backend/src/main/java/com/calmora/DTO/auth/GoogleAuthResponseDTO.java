package com.calmora.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleAuthResponseDTO {

    private boolean success;
    private String message;
    private String username;
    private String email;
}
