package com.calmora.controller;


import com.calmora.DTO.ShortRequestDTO;
import com.calmora.DTO.ShortResponseDTO;
import com.calmora.service.ShortService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/shorts")
@RequiredArgsConstructor
public class ShortVideoController {

    private final ShortService shortService;

    @PostMapping("/test")
public String test() {
    return "Working";
}

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShortResponseDTO> addShort(
                                            @ModelAttribute ShortRequestDTO request,
                                            @RequestPart("video") MultipartFile video) 
                                            throws IOException {
                                                return ResponseEntity.ok(shortService.addShort(request, video));
    }


    @GetMapping
    public ResponseEntity<List<ShortResponseDTO>> getAllShorts() {

        return ResponseEntity.ok(
                shortService.getAllShorts()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortResponseDTO> getShortById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                shortService.getShortById(id)
        );
    }
}
