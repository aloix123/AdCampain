package com.example.demo.controller;

import com.example.demo.dto.CampaignDTO;
import com.example.demo.dto.CampaignRequestDTO;
import com.example.demo.service.CampainService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    @ResponseStatus(HttpStatus.CREATED)
    public CampaignDTO createCampaign(@Valid @RequestBody CampaignRequestDTO dto){
        return campainService.createAdCampaign(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCampaignById(@PathVariable Long id){
        campainService.deleteCampaignById(id);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public CampaignDTO updateCampaignBy(@Valid @RequestBody CampaignDTO dto){
        return campainService.updateCampaign(dto);
    }

    @GetMapping("/keyword")
    @ResponseStatus(HttpStatus.OK)
    public List<String> returnAllKeyWords(){
       return campainService.getAllKeyWordsList();
    }

    @GetMapping("/town")
    @ResponseStatus(HttpStatus.OK)
    public List<String> returnAllTowns(){
        return campainService.getAllTowns();
    }



}
