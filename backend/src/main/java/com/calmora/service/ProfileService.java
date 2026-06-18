package com.calmora.service;

import org.springframework.stereotype.Service;
import com.calmora.DTO.ProfileRequestDTO;
import com.calmora.DTO.ProfileResponseDTO;  
import com.calmora.model.User;
import com.calmora.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileResponseDTO getProfile(){

        String email = SecurityContextHolder
                       .getContext()
                       .getAuthentication()
                       .getName();

        User user = userRepository.findByEmail(email)
                                  .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new ProfileResponseDTO(
            user.getUsername(),
            user.getEmail(),
            user.getPhoneNumber(),  
            user.getGender(),
            user.getDateOfBirth(),
            user.getProfileImageUrl()
        );
        
    }

    public ProfileResponseDTO updateProfile(ProfileRequestDTO profileRequestDTO){

        String email = SecurityContextHolder
                       .getContext()
                       .getAuthentication()
                       .getName();

        User user = userRepository.findByEmail(email)
                                  .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setPhoneNumber(profileRequestDTO.getPhoneNumber());
        user.setGender(profileRequestDTO.getGender());
        user.setDateOfBirth(profileRequestDTO.getDateOfBirth());
        user.setProfileImageUrl(profileRequestDTO.getProfileImageUrl());
        user.setUsername(profileRequestDTO.getUsername());

        userRepository.save(user);

        return new ProfileResponseDTO(
            user.getUsername(),
            user.getEmail(),
            user.getPhoneNumber(),  
            user.getGender(),
            user.getDateOfBirth(),
            user.getProfileImageUrl()
        );
    }
}
