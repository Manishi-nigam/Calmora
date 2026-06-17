package com.calmora.service;

import org.springframework.stereotype.Service;
import com.calmora.repository.WisdomRepository;
import com.calmora.DTO.WisdomRequestDTO;
import com.calmora.DTO.WisdomResponseDTO;
import com.calmora.model.Wisdom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WisdomService {

    public WisdomRepository wisdomRepository;

    WisdomService(WisdomRepository wisdomRepository) {
        this.wisdomRepository = wisdomRepository;
    }

    public WisdomResponseDTO addWisdom(WisdomRequestDTO req) {

        Wisdom wisdom = new Wisdom();

        wisdom.setQuote(req.getQuote());
        wisdom.setAuthor(req.getAuthor());
        Wisdom savedWisdom = wisdomRepository.save(wisdom);

        return new WisdomResponseDTO(savedWisdom.getId(),
                savedWisdom.getQuote(),
                savedWisdom.getAuthor());
    }

    public List<WisdomResponseDTO> getAllWisdoms() {
        return wisdomRepository.findAll()
                .stream()
                .map(wisdom -> new WisdomResponseDTO(
                        wisdom.getId(),
                        wisdom.getQuote(),
                        wisdom.getAuthor()))
                .collect(Collectors.toList());
    }

    public void deleteWisdom(Long id) {
        wisdomRepository.deleteById(id);
    }
}
