package com.example.demo.mapper;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;

import java.util.List;

public class ProductMapper {
    public static ProductDTO toDTO(Product product){
        return ProductDTO.builder()
                .name(product.getName())
                .id(product.getId())
                .sellerId(product.getSeller().getId())
                .build();
    }


    public static List<ProductDTO> toDTOs(List<Product> products) {
        return products.stream().map(ProductMapper::toDTO).toList();
    }
}
