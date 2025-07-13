package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@AllArgsConstructor
@RequestMapping("api/v1/product")
@RestController
public class ProductController {
    private ProductService productService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductById(@PathVariable Long id){
        return productService.findDTOById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getAllProductsBySellerId(@RequestParam Long sellerId){
        return productService.findAllBySellerId(sellerId);
    }
}
