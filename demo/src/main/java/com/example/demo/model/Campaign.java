package com.example.demo.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "you need to provide name of campain")
    private String name;
    @NotNull(message = "you need to provice bidamount")
    @DecimalMin(value="1",message = "amount must be at least 1 dollar please")
    private BigDecimal bidAmount;
    @NotNull
    private BigDecimal campaignFund;
    @NotNull(message = "you need to set your town status")
    private CampainStatus status;
    @NotNull(message = "you need to choose proper town")
    private Town town;
    @NotNull(message = "radius is required")
    @DecimalMax(value = "40075",message = "radius cannot be grater than circumference of earth")
    private int radius;

    @ElementCollection
    @Nullable
    private List<String> keywords;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = "you need to choose product you want to advertise")
    private Product product;
}
