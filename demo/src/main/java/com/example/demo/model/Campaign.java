package com.example.demo.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    private BigDecimal bidAmount;
    private BigDecimal campaignFund;
    private CampainStatus status;
    private String town;
    private int radius;

    @ElementCollection
    private List<String> keywords;

    @ManyToOne
    private Seller seller;

    @ManyToOne
    private Product product;
}
