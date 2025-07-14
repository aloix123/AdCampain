package com.example.demo.integrationTest;

import com.example.demo.dto.CampaignDTO;
import com.example.demo.dto.CampaignRequestDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.exception.ProductDataIsInwalidException;
import com.example.demo.mapper.CampaignMapper;
import com.example.demo.model.*;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.service.CampainService; // your typo here
import com.example.demo.service.ProductService;
import com.example.demo.service.SellerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CampaignServiceIntegrationTest {

    @Autowired
    private CampainService campaignService;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SellerService sellerService;


    private Seller seller;
    private Product product;
    private Campaign campaign;

    @BeforeEach
    public void setup() {
        campaignRepository.deleteAll();
        productRepository.deleteAll();
        sellerRepository.deleteAll();

        seller = new Seller();
        seller.setName("Test Seller");
        seller.setEmeraldBalance(BigDecimal.valueOf(10000));
        seller = sellerRepository.saveAndFlush(seller);

        product = new Product();
        product.setName("Test Product");
        product.setSeller(seller);
        product = productRepository.saveAndFlush(product);

        campaign = new Campaign();
        campaign.setProduct(product);
        campaign.setCampaignFund(BigDecimal.valueOf(1000));
        campaign.setName("Test Campaign");
        campaign.setStatus(CampainStatus.ON);
        campaign.setTown(Town.BERLIN);
        campaign.setRadius(10);
        campaign.setBidAmount(BigDecimal.valueOf(5.00));
        campaign.setKeywords(List.of("electronics"));
        campaign = campaignRepository.saveAndFlush(campaign);
    }

    @Test
    public void testGetAllCampainsBySellerId_ReturnsCampaigns() {
        List<CampaignDTO> campaigns = campaignService.getAllCampainsBySellerId(seller.getId());
        assertNotNull(campaigns);
        assertFalse(campaigns.isEmpty());
        assertEquals(campaign.getId(), campaigns.get(0).getId());
    }

    @Test
    public void testCreateAdCampaign_Success() {
        CampaignRequestDTO dto = new CampaignRequestDTO();
        ProductDTO productDTO = productService.findDTOById(product.getId());
        dto.setProductDTO(productDTO);
        dto.setCampaignFund(BigDecimal.valueOf(500));

        dto.setName("New Campaign Name");
        dto.setStatus(CampainStatus.ON);
        dto.setTown(Town.BERLIN);
        dto.setRadius(10);
        dto.setBidAmount(BigDecimal.valueOf(10));
        dto.setKeywords(List.of("electronics"));

        BigDecimal balanceBefore = sellerService.getById(seller.getId()).getEmeraldBalance();

        CampaignDTO createdCampaign = campaignService.createAdCampaign(dto);

        assertNotNull(createdCampaign);
        assertEquals(dto.getCampaignFund(), createdCampaign.getCampaignFund());

        BigDecimal balanceAfter = sellerService.getById(seller.getId()).getEmeraldBalance();
        assertEquals(balanceBefore.subtract(dto.getCampaignFund()), balanceAfter);
    }

    @Test
    public void testCreateAdCampaign_ProductDataInvalid_ThrowsException() {
        CampaignRequestDTO dto = new CampaignRequestDTO();
        ProductDTO productDTO = productService.findDTOById(product.getId());
        productDTO.setName("Changed Name"); // Modify to invalidate
        dto.setProductDTO(productDTO);
        dto.setCampaignFund(BigDecimal.valueOf(500));

        dto.setName("Invalid Campaign");
        dto.setStatus(CampainStatus.ON);
        dto.setTown(Town.BERLIN);
        dto.setRadius(10);
        dto.setBidAmount(BigDecimal.valueOf(10));
        dto.setKeywords(List.of("electronics"));

        assertThrows(ProductDataIsInwalidException.class, () -> campaignService.createAdCampaign(dto));
    }

    @Test
    public void testDeleteCampaignById_Success() {
        campaignService.deleteCampaignById(campaign.getId());
        assertFalse(campaignRepository.findById(campaign.getId()).isPresent());
    }

    @Test
    public void testUpdateCampaign_Success() {
        CampaignDTO dto = CampaignMapper.toDTO(campaign);
        dto.setCampaignFund(BigDecimal.valueOf(800));

        BigDecimal oldBalance = sellerService.getById(seller.getId()).getEmeraldBalance();

        CampaignDTO updated = campaignService.updateCampaign(dto);
        assertEquals(dto.getCampaignFund(), updated.getCampaignFund());

        BigDecimal newBalance = sellerService.getById(seller.getId()).getEmeraldBalance();
        assertEquals(oldBalance.add(BigDecimal.valueOf(200)), newBalance);
    }
    @Test
    public void testFindById_ReturnsCampaign() {
        Campaign found = campaignService.findById(campaign.getId());
        assertNotNull(found);
        assertEquals(campaign.getId(), found.getId());
    }

    @Test
    public void testGetAllKeyWordsList_ReturnsNonEmptyList() {
        List<String> keywords = campaignService.getAllKeyWordsList();
        assertNotNull(keywords);
        assertTrue(keywords.contains("electronics"));
    }

    @Test
    public void testGetAllTowns_ReturnsAllTowns() {
        List<String> towns = campaignService.getAllTowns();
        assertNotNull(towns);
        assertTrue(towns.contains("BERLIN"));
    }
}
