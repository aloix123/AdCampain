package com.example.demo.mapper;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product product){
        return ProductDTO.builder()
                .name(product.getName())
                .sellerId(product.getId())
                .build();
    }

    public static Product toEntity(ProductDTO dto){
        return Product.builder()
                .name(dto.getName())
                .build();
    }
}
