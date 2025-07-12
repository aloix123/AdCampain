package com.example.demo.dto;

import com.example.demo.model.CampainStatus;
import com.example.demo.model.Town;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CampaignDTO {
    private String name;
    private BigDecimal bidAmount;
    private BigDecimal campaignFund;
    private CampainStatus status;
    private Town town;
    private int radius;
    private List<String> keywords;
    private ProductDTO productDTO;
}
