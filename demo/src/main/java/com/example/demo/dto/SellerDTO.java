package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Builder
@Getter
@Setter
public class SellerDTO {
    private Long id;

    private String name;

    private BigDecimal emeraldBalance;
}
