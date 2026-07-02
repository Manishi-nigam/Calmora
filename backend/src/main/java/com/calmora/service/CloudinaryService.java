package com.calmora.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.calmora.DTO.VideoUploadResponseDTO;


@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        Map<?, ?> uploadResult = cloudinary.uploader()
                                            .upload(file.getBytes(), ObjectUtils
                                            .asMap("resource_type", "auto"));
        return uploadResult.get("secure_url").toString();
    }

public VideoUploadResponseDTO uploadVideo(MultipartFile file) throws IOException {

    if (file.isEmpty()) {
        throw new IllegalArgumentException("Video cannot be empty");
    }

    Map<?, ?> uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", "calmora/shorts"
            )
    );

    String videoUrl = uploadResult.get("secure_url").toString();

    String publicId = uploadResult.get("public_id").toString();

    Integer duration = ((Number) uploadResult.get("duration")).intValue();

    String thumbnailUrl = cloudinary.url()
            .resourceType("video")
            .format("jpg")
            .generate(publicId);

    return new VideoUploadResponseDTO(
            videoUrl,
            thumbnailUrl,
            publicId,
            duration
    );
}
}
