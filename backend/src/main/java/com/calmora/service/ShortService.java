package com.calmora.service;


import com.calmora.DTO.ShortRequestDTO;
import com.calmora.DTO.ShortResponseDTO;
import com.calmora.model.User;
import com.calmora.repository.ShortVideoRepository;
import com.calmora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.calmora.model.ShortVideo;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShortService {
    @Autowired
    private UserRepository UserRepository;


    public ShortVideoRepository shortVideoRepository;

    ShortService(ShortVideoRepository shortVideoRepository){
        this.shortVideoRepository = shortVideoRepository;
    }

    public ShortResponseDTO createShort(ShortRequestDTO req) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        ShortVideo shortVideo = new ShortVideo();

        shortVideo.setTitle(req.getTitle());
        shortVideo.setDescription(req.getDescription());
        shortVideo.setThumbnailUrl(req.getThumbnailUrl());
        shortVideo.setVideoUrl(req.getVideoUrl());
        shortVideo.setDuration(req.getDuration());
        shortVideo.setCategory(req.getCategory());
        shortVideo.setCreatedAt(LocalDateTime.now());

        User user = UserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        ShortVideo savedShortVideo = shortVideoRepository.save(shortVideo);

        return new ShortResponseDTO(
                savedShortVideo.getId(),
                savedShortVideo.getTitle(),
                savedShortVideo.getDescription(),
                savedShortVideo.getThumbnailUrl(),
                savedShortVideo.getVideoUrl(),
                savedShortVideo.getDuration(),
                savedShortVideo.getCategory()
        );
    }

    public List<ShortResponseDTO> getAllShorts() {

        List<ShortVideo> shortVideos = shortVideoRepository.findAll();

        return shortVideos.stream()
                .map(shortVideoVideo -> new ShortResponseDTO(
                        shortVideoVideo.getId(),
                        shortVideoVideo.getTitle(),
                        shortVideoVideo.getDescription(),
                        shortVideoVideo.getThumbnailUrl(),
                        shortVideoVideo.getVideoUrl(),
                        shortVideoVideo.getDuration(),
                        shortVideoVideo.getCategory()
                ))
                .toList();
    }


    public ShortResponseDTO getShortById(Long id) {

        ShortVideo shortVideo = shortVideoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Short not found"));;

        return new ShortResponseDTO(
                shortVideo.getId(),
                shortVideo.getTitle(),
                shortVideo.getDescription(),
                shortVideo.getThumbnailUrl(),
                shortVideo.getVideoUrl(),
                shortVideo.getDuration(),
                shortVideo.getCategory()
        );
    }

}
