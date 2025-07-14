package com.example.demo.integrationTest;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceIntegrationTest {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private Seller testSeller;
    private Product testProduct;

    @BeforeEach
    public void setup() {
        // Order matters: Campaigns → Products → Sellers
        campaignRepository.deleteAll();
        productRepository.deleteAll();
        sellerRepository.deleteAll();

        testSeller = new Seller();
        testSeller.setName("Test Seller");
        testSeller.setEmeraldBalance(java.math.BigDecimal.valueOf(5000));
        sellerRepository.save(testSeller);

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setSeller(testSeller);
        productRepository.save(testProduct);
    }

    @Test
    public void testFindById_ReturnsProduct() {
        Product product = productService.findById(testProduct.getId());
        assertNotNull(product);
        assertEquals(testProduct.getName(), product.getName());
        assertEquals(testSeller.getId(), product.getSeller().getId());
    }

    @Test
    public void testFindById_ThrowsExceptionForInvalidId() {
        Long invalidId = 9999L;
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            productService.findById(invalidId);
        });
        assertTrue(thrown.getMessage().contains("Product not found"));
    }

    @Test
    public void testFindDTOById_ReturnsCorrectDTO() {
        ProductDTO dto = productService.findDTOById(testProduct.getId());
        assertNotNull(dto);
        assertEquals(testProduct.getId(), dto.getId());
        assertEquals(testProduct.getName(), dto.getName());
        assertEquals(testSeller.getId(), dto.getSellerId());
    }

    @Test
    public void testCheckIfProductIsUnchanged_ReturnsTrueWhenUnchanged() {
        ProductDTO dto = productService.findDTOById(testProduct.getId());
        boolean unchanged = productService.checkIfProductIsUnchanged(testProduct, dto);
        assertTrue(unchanged);
    }

    @Test
    public void testCheckIfProductIsUnchanged_ReturnsFalseWhenChanged() {
        ProductDTO dto = productService.findDTOById(testProduct.getId());
        dto.setName("Modified Name");
        boolean unchanged = productService.checkIfProductIsUnchanged(testProduct, dto);
        assertFalse(unchanged);
    }

    @Test
    public void testFindAllBySellerId_ReturnsProducts() {
        List<ProductDTO> products = productService.findAllBySellerId(testSeller.getId());
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals(testProduct.getId(), products.get(0).getId());
    }
}
