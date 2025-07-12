package com.example.demo.controller;

import com.example.demo.dto.CampaignDTO;
import com.example.demo.service.CampainService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/campaign")
public class CampaignController {
    private CampainService campainService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<CampaignDTO> getAllBySellerId(@PathVariable Long id){
        return campainService.getAllCampainsBySellerId(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void createAddCampaign(@Valid @RequestBody CampaignDTO dto){
        campainService.createAdCampain(dto);
    }

}
