package com.example.demo.service;

import com.example.demo.dto.CampaignDTO;
import com.example.demo.mapper.CampaignMapper;
import com.example.demo.model.Campaign;
import com.example.demo.model.Product;
import com.example.demo.repository.CampaignRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CampainService {
    private CampaignRepository campaignRepository;
    private ProductService productService;

    public List<CampaignDTO> getAllCampainsBySellerId(Long sellerId) {
        List<Campaign> campaigns = campaignRepository.getAllBySellerId(sellerId);
        return CampaignMapper.toDtos(campaigns);
    }

    public void createAdCampain(CampaignDTO dto) {
        Product product = productService.findByName(dto.getProductDTO().getName());
        Campaign campaign=CampaignMapper.toEntity(dto);
        campaign.setProduct(product);
        campaignRepository.save(campaign);
    }
}