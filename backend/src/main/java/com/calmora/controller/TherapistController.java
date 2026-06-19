package com.calmora.controller;

import com.calmora.DTO.TherapistDetailsDTO;
import com.calmora.DTO.TherapistListDTO;
import com.calmora.model.Therapist;
import com.calmora.service.TherapistService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.calmora.repository.TherapistRepository;

@RestController
@RequestMapping("/api/therapists")
public class TherapistController {

    private final TherapistService therapistService;
    private final TherapistRepository therapistRepository;

    public TherapistController(
            TherapistService therapistService,
            TherapistRepository therapistRepository) {
        this.therapistService = therapistService;
        this.therapistRepository = therapistRepository;
    }

    @PostMapping
    public Therapist createTherapist(
        @RequestBody Therapist therapist) {

            return therapistRepository.save(therapist);
        }

    @GetMapping
    public List<TherapistListDTO> getAllTherapists() {
        return therapistService.getAllTherapists();
    }

    @GetMapping("/{id}")
    public TherapistDetailsDTO getTherapistById(
            @PathVariable Long id) {

        return therapistService.getTherapistById(id);
    }
}
