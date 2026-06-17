package com.calmora.controller;


import com.calmora.DTO.WisdomRequestDTO;
import com.calmora.DTO.WisdomResponseDTO;
import com.calmora.service.WisdomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wisdom")
public class WisdomController {

    @Autowired
    private WisdomService wisdomService;

    @PostMapping
    public WisdomResponseDTO addWisdom(
            @RequestBody WisdomRequestDTO request
    ) {
        return wisdomService.addWisdom(request);
    }

    @GetMapping
    public List<WisdomResponseDTO> getAllWisdoms() {
        return wisdomService.getAllWisdoms();
    }

    @GetMapping("/today")
    public WisdomResponseDTO getDailyWisdom() {
    return wisdomService.getDailyWisdom();
}

    @DeleteMapping("/{id}")
    public String deleteWisdom(@PathVariable Long id) {

        wisdomService.deleteWisdom(id);

        return "Wisdom deleted successfully";
    }
}
