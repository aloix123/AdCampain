package com.example.demo.mapper;

import com.example.demo.dto.CampaignDTO;
import com.example.demo.model.Campaign;

import java.util.List;
import java.util.stream.Collectors;

public class CampaignMapper {

    public static CampaignDTO toDTO(Campaign campaign){
        return CampaignDTO.builder()
                .name(campaign.getName())
                .town(campaign.getTown())
                .bidAmount(campaign.getBidAmount())
                .radius(campaign.getRadius())
                .status(campaign.getStatus())
                .campaignFund(campaign.getCampaignFund())
                .keywords(campaign.getKeywords())
                .productDTO(ProductMapper.toDTO(campaign.getProduct()))
                .build();

    }
    public static List<CampaignDTO> toDtos(List<Campaign> campaigns) {
        return campaigns.stream()
                .map(CampaignMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Campaign toEntity(CampaignDTO dto) {
        return Campaign.builder()
                .name(dto.getName())
                .campaignFund(dto.getCampaignFund())
                .radius(dto.getRadius())
                .town(dto.getTown())
                .status(dto.getStatus())
                .bidAmount(dto.getBidAmount())
                .keywords(dto.getKeywords())
                .build();
    }
}
