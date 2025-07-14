package com.example.demo.dto;
import com.example.demo.model.CampainStatus;
import com.example.demo.model.Town;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CampaignDTO {
    private Long id;

    @NotBlank(message = "Campaign name is required")
    private String name;

    @NotNull(message = "Bid amount is required")
    @DecimalMin(value = "0.01", message = "Bid amount must be at least 0.01")
    private BigDecimal bidAmount;

    @NotNull(message = "Campaign fund is required")
    @DecimalMin(value = "1", message = "Campaign fund must be at least 1.0")
    private BigDecimal campaignFund;

    @NotNull(message = "Status is required")
    private CampainStatus status;

    @NotNull(message = "Town is required")
    private Town town;

    @Min(value = 1, message = "Radius must be greater than 0")
    @Max(value = 40075, message = "Radius cannot exceed the Earth's circumference")
    private int radius;

    @NotNull(message = "Keywords are required")
    @Size(min = 1, message = "At least one keyword must be provided")
    private List<@NotBlank String> keywords;

    @NotNull(message = "Product information is required")
    private ProductDTO productDTO;
}
