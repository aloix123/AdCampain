package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class ProductService {
    private ProductRepository productRepository;

    public Product findByName(String name){
        return productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + name));
    }
}
