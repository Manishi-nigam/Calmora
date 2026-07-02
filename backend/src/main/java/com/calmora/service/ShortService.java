package com.calmora.service;

import com.calmora.DTO.ShortRequestDTO;
import com.calmora.DTO.ShortResponseDTO;
import com.calmora.DTO.VideoUploadResponseDTO;
import com.calmora.model.ShortVideo;
import com.calmora.repository.ShortVideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShortService {

    private final ShortVideoRepository shortVideoRepository;
    private final CloudinaryService cloudinaryService;

    public ShortService(ShortVideoRepository shortVideoRepository,
                        CloudinaryService cloudinaryService) {

        this.shortVideoRepository = shortVideoRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public ShortResponseDTO addShort(ShortRequestDTO req,
                                     MultipartFile video) throws IOException {

        VideoUploadResponseDTO uploadResponse =
                cloudinaryService.uploadVideo(video);

        ShortVideo shortVideo = new ShortVideo();

        shortVideo.setTitle(req.getTitle());
        shortVideo.setDescription(req.getDescription());
        shortVideo.setCategory(req.getCategory());

        shortVideo.setVideoUrl(uploadResponse.getVideoUrl());
        shortVideo.setThumbnailUrl(uploadResponse.getThumbnailUrl());
        shortVideo.setPublicId(uploadResponse.getPublicId());
        shortVideo.setDuration(uploadResponse.getDuration());

        shortVideo.setCreatedAt(LocalDateTime.now());

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
                .map(shortVideo -> new ShortResponseDTO(
                        shortVideo.getId(),
                        shortVideo.getTitle(),
                        shortVideo.getDescription(),
                        shortVideo.getThumbnailUrl(),
                        shortVideo.getVideoUrl(),
                        shortVideo.getDuration(),
                        shortVideo.getCategory()
                ))
                .toList();
    }

    public ShortResponseDTO getShortById(Long id) {

        ShortVideo shortVideo = shortVideoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Short not found"));

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