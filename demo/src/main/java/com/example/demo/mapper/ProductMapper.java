package com.example.demo.mapper;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product product){
        return ProductDTO.builder()
                .name(product.getName())
                .id(product.getId())
                .sellerId(product.getId())
                .build();
    }


}
