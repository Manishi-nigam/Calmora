package com.calmora.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.calmora.DTO.ProfileRequestDTO;
import com.calmora.DTO.ProfileResponseDTO;  
import com.calmora.model.User;
import com.calmora.repository.UserRepository;
import com.calmora.service.CloudinaryService;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public ProfileService(UserRepository userRepository, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
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

     public String uploadProfileImage(MultipartFile file)
        throws IOException {

    String email = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    String imageUrl = cloudinaryService.uploadImage(file);

    user.setProfileImageUrl(imageUrl);

    userRepository.save(user);

    return imageUrl;
}
}
