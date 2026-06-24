package com.calmora.service;

import com.calmora.DTO.TherapistDetailsDTO;
import com.calmora.DTO.TherapistListDTO;
import com.calmora.model.Therapist;
import com.calmora.repository.TherapistRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TherapistService {

    private final TherapistRepository therapistRepository;

    public TherapistService(
            TherapistRepository therapistRepository) {
        this.therapistRepository = therapistRepository;
    }

    public List<TherapistListDTO> getAllTherapists() {

        return therapistRepository.findAll()
                .stream()
                .map(t -> new TherapistListDTO(
                        t.getId(),
                        t.getName(),
                        t.getSpecialization(),
                        t.getExperience(),
                        t.getImageUrl(),
                        t.getQualifications(),
                        t.getSpecializations(),
                        t.getConsultationFee()
                ))
                .toList();
    }

    public TherapistDetailsDTO getTherapistById(Long id) {

        Therapist therapist = therapistRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Therapist not found"));

        return new TherapistDetailsDTO(
                therapist.getId(),
                therapist.getName(),
                therapist.getSpecialization(),
                therapist.getExperience(),
                therapist.getImageUrl(),
                therapist.getBio(),
                therapist.getEmail(),
                therapist.getClientsServed(),
                therapist.getConsultationFee(),
                therapist.getQualifications(),
                therapist.getSpecializations()
        );
    }
}
