package com.example.demo.service;

import com.example.demo.dto.CampaignDTO;
import com.example.demo.dto.CampaignRequestDTO;
import com.example.demo.exception.ProductDataIsInwalidException;
import com.example.demo.mapper.CampaignMapper;
import com.example.demo.model.Campaign;
import com.example.demo.model.Product;
import com.example.demo.repository.CampaignRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@AllArgsConstructor
@Service
public class CampainService {
    private CampaignRepository campaignRepository;
    private ProductService productService;
    private SellerService sellerService;

    public List<CampaignDTO> getAllCampainsBySellerId(Long sellerId) {
        List<Campaign> campaigns = campaignRepository.getAllBySellerId(sellerId);
        return CampaignMapper.toDtos(campaigns);
    }

    public void createAdCampaign(CampaignRequestDTO dto) {
        Product product = productService.findById(dto.getProductDTO().getId());
        if(productService.checkIfProductIsUnchanged(product,dto.getProductDTO())){
            Campaign campaign=CampaignMapper.toEntity(dto);
            campaign.setProduct(product);
            sellerService.updateSellerAccountBySellerId(product.getSeller().getId(),campaign.getCampaignFund());
            campaignRepository.save(campaign);
        }
        else{
            throw new ProductDataIsInwalidException();
        }

    }

    public void deleteCampaignById(Long id) {
        Campaign campaign =findById(id);
        campaignRepository.deleteById(id);
    }

    public void updateCampaign(@Valid CampaignDTO dto) {
        Campaign existingCampaign = findById(dto.getId());
        Product product = productService.findById(dto.getProductDTO().getId());
        CampaignMapper.updateEntity(existingCampaign,dto,product);
        campaignRepository.save(existingCampaign);
    }

    public Campaign findById(Long id){
        return campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));
    }

    public List<String> getAllKeyWordsList() {
        return List.of(
                "electronics",
                "fashion",
                "fitness",
                "books",
                "gaming",
                "furniture",
                "kitchen",
                "travel",
                "beauty",
                "toys"
        );
    }

}