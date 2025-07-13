package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.SellerMapper;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class ProductService {
    private ProductRepository productRepository;

    public Product findById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public ProductDTO findDTOById(Long id){
       return  ProductMapper.toDTO(findById(id));
    }

    public boolean checkIfProductIsUnchanged(Product product, ProductDTO dto){
        return product.getId().equals(dto.getId()) && product.getName().equals(dto.getName())
                && product.getSeller().getId().equals(dto.getSellerId());

    }
}
