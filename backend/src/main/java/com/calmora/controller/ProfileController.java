package com.calmora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.calmora.DTO.ProfileRequestDTO;
import com.calmora.DTO.ProfileResponseDTO;
import com.calmora.service.ProfileService;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ProfileResponseDTO getProfile() {
        return profileService.getProfile();
    }

    @PutMapping
    public ProfileResponseDTO updateProfile(
            @RequestBody ProfileRequestDTO request
    ) {
        return profileService.updateProfile(request);
    }
}