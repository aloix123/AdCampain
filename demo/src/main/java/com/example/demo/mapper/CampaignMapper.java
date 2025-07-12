package com.example.demo.mapper;

import com.example.demo.dto.CampainDTO;
import com.example.demo.model.Campaign;

import java.util.List;
import java.util.stream.Collectors;

public class CampaignMapper {

    public static CampainDTO toDTO(Campaign campaign){
        return CampainDTO.builder()
                .name(campaign.getName())
                .town(campaign.getTown())
                .bidAmount(campaign.getBidAmount())
                .radius(campaign.getRadius())
                .status(campaign.getStatus())
                .campaignFund(campaign.getCampaignFund())
                .keywords(campaign.getKeywords())
                .product(campaign.getProduct())
                .build();

    }
    public static List<CampainDTO> toDtos(List<Campaign> campaigns) {
        return campaigns.stream()
                .map(CampaignMapper::toDTO)
                .collect(Collectors.toList());
    }

}
