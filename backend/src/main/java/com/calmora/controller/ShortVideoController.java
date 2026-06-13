package com.calmora.controller;


import com.calmora.DTO.ShortRequestDTO;
import com.calmora.DTO.ShortResponseDTO;
import com.calmora.service.ShortService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shorts")
@RequiredArgsConstructor
public class ShortVideoController {

    private final ShortService shortService;

    @PostMapping
    public ResponseEntity<ShortResponseDTO> createShort(
            @RequestBody ShortRequestDTO request) {

        return ResponseEntity.ok(
                shortService.createShort(request)
        );
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
