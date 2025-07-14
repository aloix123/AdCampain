
package com.example.demo.unitTest;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setup() {
        Seller seller = new Seller();
        seller.setId(1L);

        product = new Product();
        product.setId(100L);
        product.setName("Test Product");
        product.setSeller(seller);

        productDTO = new ProductDTO(100L, "Test Product", 1L);
    }

    @Test
    void findById_shouldReturnProduct_whenFound() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        Product result = productService.findById(100L);

        assertThat(result).isEqualTo(product);
        verify(productRepository).findById(100L);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(productRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(100L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 100");
    }

    @Test
    void findDTOById_shouldReturnProductDTO() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        ProductDTO dto = productService.findDTOById(100L);

        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getName()).isEqualTo("Test Product");
        assertThat(dto.getSellerId()).isEqualTo(1L);
    }

    @Test
    void checkIfProductIsUnchanged_shouldReturnTrue_whenProductMatchesDTO() {
        boolean result = productService.checkIfProductIsUnchanged(product, productDTO);
        assertThat(result).isTrue();
    }

    @Test
    void checkIfProductIsUnchanged_shouldReturnFalse_whenProductDiffersFromDTO() {
        ProductDTO differentDTO = new ProductDTO(101L, "Other Product", 2L);
        boolean result = productService.checkIfProductIsUnchanged(product, differentDTO);
        assertThat(result).isFalse();
    }

    @Test
    void findAllBySellerId_shouldReturnDTOList() {
        when(productRepository.findBySellerId(1L)).thenReturn(List.of(product));

        List<ProductDTO> result = productService.findAllBySellerId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(100L);
    }
}
