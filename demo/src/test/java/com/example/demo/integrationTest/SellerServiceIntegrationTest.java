package com.example.demo.integrationTest;


import com.example.demo.exception.NoMoneyException;
import com.example.demo.model.Seller;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SellerServiceIntegrationTest {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerRepository sellerRepository;

    private Seller testSeller;

    @Autowired
    private ProductRepository productRepository; // if you have it

    @BeforeEach
    public void setup() {
        // Delete dependent entities first to respect foreign key constraints
        campaignRepository.deleteAll();
        productRepository.deleteAll();
        sellerRepository.deleteAll();

        testSeller = new Seller();
        testSeller.setEmeraldBalance(BigDecimal.valueOf(2000));
        testSeller.setName("Test Seller");
        sellerRepository.save(testSeller);
    }



    @Test
    public void testUpdateSellerAccountBySellerId_success() {
        BigDecimal deductAmount = BigDecimal.valueOf(500);

        sellerService.updateSellerAccountBySellerId(testSeller.getId(), deductAmount);

        Seller updated = sellerRepository.findById(testSeller.getId()).orElseThrow();
        assertEquals(BigDecimal.valueOf(1500), updated.getEmeraldBalance());
    }

    @Test
    public void testUpdateSellerAccountBySellerId_insufficientFunds() {
        BigDecimal deductAmount = BigDecimal.valueOf(3000);

        NoMoneyException exception = assertThrows(NoMoneyException.class, () -> {
            sellerService.updateSellerAccountBySellerId(testSeller.getId(), deductAmount);
        });

        assertEquals("you dont have much money to create this campain ", exception.getMessage());
    }

    @Test
    public void testIncreaseAccountValue_success() {
        BigDecimal newBalance = sellerService.increaseAccountValue(testSeller.getId());

        assertEquals(BigDecimal.valueOf(3000), newBalance);

        Seller updated = sellerRepository.findById(testSeller.getId()).orElseThrow();
        assertEquals(BigDecimal.valueOf(3000), updated.getEmeraldBalance());
    }

    @Test
    public void testUpdateSellerAccountAfterUpdateBySellerId_decreaseFund() {
        BigDecimal oldFund = BigDecimal.valueOf(200);
        BigDecimal newFund = BigDecimal.valueOf(300); // Increased fund by 100

        sellerService.updateSellerAccountAfterUpdateBySellerId(testSeller.getId(), oldFund, newFund);

        Seller updated = sellerRepository.findById(testSeller.getId()).orElseThrow();
        // New balance = 2000 - (300-200) = 1900
        assertEquals(BigDecimal.valueOf(1900), updated.getEmeraldBalance());
    }

    @Test
    public void testUpdateSellerAccountAfterUpdateBySellerId_increaseFund() {
        BigDecimal oldFund = BigDecimal.valueOf(300);
        BigDecimal newFund = BigDecimal.valueOf(200); // Decreased fund by 100

        sellerService.updateSellerAccountAfterUpdateBySellerId(testSeller.getId(), oldFund, newFund);

        Seller updated = sellerRepository.findById(testSeller.getId()).orElseThrow();
        // New balance = 2000 - (200-300) = 2000 + 100 = 2100
        assertEquals(BigDecimal.valueOf(2100), updated.getEmeraldBalance());
    }

    @Test
    public void testUpdateSellerAccountAfterUpdateBySellerId_insufficientFunds() {
        BigDecimal oldFund = BigDecimal.valueOf(100);
        BigDecimal newFund = BigDecimal.valueOf(3000); // Huge increase => large negative balance

        NoMoneyException exception = assertThrows(NoMoneyException.class, () -> {
            sellerService.updateSellerAccountAfterUpdateBySellerId(testSeller.getId(), oldFund, newFund);
        });

        assertEquals("you dont have much money to create this campain ", exception.getMessage());
    }

    @Test
    public void testGetById_success() {
        var dto = sellerService.getById(testSeller.getId());
        assertNotNull(dto);
        assertEquals(testSeller.getId(), dto.getId());
        assertEquals(testSeller.getEmeraldBalance(), dto.getEmeraldBalance());
    }

    @Test
    public void testGetById_notFound() {
        Long invalidId = 999L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sellerService.getById(invalidId);
        });

        assertTrue(exception.getMessage().contains("Seller not found"));
    }

}
