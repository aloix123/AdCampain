package com.example.demo.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Campaign name is required")
    private String name;

    @NotNull(message = "Bid amount is required")
    @DecimalMin(value = "1.00", message = "Bid amount must be at least 1")
    private BigDecimal bidAmount;

    @NotNull(message = "Campaign fund is required")
    @DecimalMin(value = "1.00", message = "Campaign fund must be at least 1")
    private BigDecimal campaignFund;

    @NotNull(message = "Campaign status is required")
    private CampainStatus status;

    @NotNull(message = "You need to choose a town")
    private Town town;

    @Min(value = 1, message = "Radius must be at least 1 km")
    @Max(value = 40075, message = "Radius cannot exceed Earth's circumference")
    private int radius;

    @ElementCollection
    @NotEmpty(message = "At least one keyword is required")
    private List<String> keywords;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = "You must choose a product to advertise")
    private Product product;
}

