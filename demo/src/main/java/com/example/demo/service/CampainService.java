package com.example.demo.service;

import com.example.demo.dto.CampainDTO;
import com.example.demo.mapper.CampaignMapper;
import com.example.demo.model.Campaign;
import com.example.demo.repository.CampaignRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CampainService {
    private CampaignRepository campainRepository;

    public List<CampainDTO> getAllCampainsBySellerId(Long sellerId){
        List<Campaign> campaigns = campainRepository.getAllBySellerId(sellerId);
        return CampaignMapper.toDtos(campaigns);
    }
}
