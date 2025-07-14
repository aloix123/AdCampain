package com.example.demo.service;

import com.example.demo.dto.CampaignDTO;
import com.example.demo.dto.CampaignRequestDTO;
import com.example.demo.exception.ProductDataIsInwalidException;
import com.example.demo.mapper.CampaignMapper;
import com.example.demo.model.Campaign;
import com.example.demo.model.Product;
import com.example.demo.model.Town;
import com.example.demo.repository.CampaignRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public CampaignDTO createAdCampaign(CampaignRequestDTO dto) {
        Product product = productService.findById(dto.getProductDTO().getId());

        if (productService.checkIfProductIsUnchanged(product, dto.getProductDTO())) {
            Campaign campaign = CampaignMapper.toEntity(dto);
            campaign.setProduct(product);

            sellerService.updateSellerAccountBySellerId(product.getSeller().getId(), campaign.getCampaignFund());

            campaignRepository.save(campaign);
            return CampaignMapper.toDTO(campaign);
        } else {
            throw new ProductDataIsInwalidException();
        }
    }
    public void deleteCampaignById(Long id) {
        if (!campaignRepository.existsById(id)) {
            throw new EntityNotFoundException("Campaign with id " + id + " does not exist.");
        }
        campaignRepository.deleteById(id);
    }

    public CampaignDTO updateCampaign(@Valid CampaignDTO dto) {
        Campaign existingCampaign = findById(dto.getId());
        Product product = productService.findById(dto.getProductDTO().getId());

        sellerService.updateSellerAccountAfterUpdateBySellerId(
                dto.getProductDTO().getSellerId(),
                existingCampaign.getCampaignFund(),
                dto.getCampaignFund()
        );

        CampaignMapper.updateEntity(existingCampaign, dto, product);
        campaignRepository.save(existingCampaign);
        return CampaignMapper.toDTO(existingCampaign);
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

    public List<String> getAllTowns() {
        return Arrays.stream(Town.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}