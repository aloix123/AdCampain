package com.example.demo.mapper;

import com.example.demo.dto.SellerDTO;
import com.example.demo.model.Seller;

import java.util.List;

public class SellerMapper {
    public static SellerDTO toDTO(Seller seller){
        return SellerDTO.builder()
                .id(seller.getId())
                .name(seller.getName())
                .emeraldBalance(seller.getEmeraldBalance())
                .build();
    }

    public static List<SellerDTO> toDTOs(List<Seller> sellers){
        return sellers.stream().map(SellerMapper::toDTO).toList();
    }

}
