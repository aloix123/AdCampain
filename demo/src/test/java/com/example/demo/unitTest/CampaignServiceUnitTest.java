package com.example.demo.unitTest;

import com.example.demo.dto.CampaignDTO;
import com.example.demo.dto.CampaignRequestDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.exception.NoMoneyException;
import com.example.demo.exception.ProductDataIsInwalidException;
import com.example.demo.mapper.CampaignMapper;
import com.example.demo.model.Campaign;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.model.Town;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.service.CampainService;
import com.example.demo.service.ProductService;
import com.example.demo.service.SellerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceUnitTest {

    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private ProductService productService;
    @Mock
    private SellerService sellerService;

    @InjectMocks
    private CampainService campainService;

    private Campaign campaign;
    private Product product;
    private Seller seller;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        seller.setId(1L);

        product = new Product();
        product.setId(100L);
        product.setSeller(seller);

        campaign = new Campaign();
        campaign.setId(10L);
        campaign.setCampaignFund(BigDecimal.valueOf(100));
        campaign.setProduct(product);
    }

    @Test
    void getAllCampaignsBySellerId_shouldReturnListOfCampaignDTOs() {
        when(campaignRepository.getAllBySellerId(1L)).thenReturn(List.of(campaign));

        var result = campainService.getAllCampainsBySellerId(1L);

        assertThat(result).hasSize(1);
        verify(campaignRepository).getAllBySellerId(1L);
    }

    @Test
    void createAdCampaign_shouldCreateCampaignWhenProductMatches() {
        CampaignRequestDTO dto = new CampaignRequestDTO();
        dto.setProductDTO(new ProductDTO(100L, "Product", 1L));
        dto.setCampaignFund(BigDecimal.valueOf(100));

        when(productService.findById(100L)).thenReturn(product);
        when(productService.checkIfProductIsUnchanged(any(), any())).thenReturn(true);

        Campaign resultEntity = CampaignMapper.toEntity(dto);
        resultEntity.setProduct(product);

        var result = campainService.createAdCampaign(dto);

        verify(sellerService).updateSellerAccountBySellerId(1L, dto.getCampaignFund());
        verify(campaignRepository).save(any());
        assertThat(result).isNotNull();
    }

    @Test
    void createAdCampaign_shouldThrowWhenProductDataInvalid() {
        CampaignRequestDTO dto = new CampaignRequestDTO();
        dto.setProductDTO(new ProductDTO(100L, "Product", 1L));

        when(productService.findById(100L)).thenReturn(product);
        when(productService.checkIfProductIsUnchanged(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> campainService.createAdCampaign(dto))
                .isInstanceOf(ProductDataIsInwalidException.class);
    }

    @Test
    void deleteCampaignById_shouldDeleteIfExists() {
        // Arrange
        when(campaignRepository.existsById(10L)).thenReturn(true);

        // Act
        campainService.deleteCampaignById(10L);

        // Assert
        verify(campaignRepository).deleteById(10L);
    }

    @Test
    void deleteCampaignById_shouldThrowIfNotFound() {
        // Arrange
        when(campaignRepository.existsById(10L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> campainService.deleteCampaignById(10L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Campaign with id 10 does not exist.");
    }


    @Test
    void updateCampaign_shouldUpdateAndAdjustSellerBalance() {
        CampaignDTO dto = new CampaignDTO();
        dto.setId(10L);
        dto.setCampaignFund(BigDecimal.valueOf(200));
        ProductDTO productDTO = new ProductDTO(100L, "Product", 1L);
        dto.setProductDTO(productDTO);

        when(campaignRepository.findById(10L)).thenReturn(Optional.of(campaign));
        when(productService.findById(100L)).thenReturn(product);

        var result = campainService.updateCampaign(dto);

        verify(sellerService).updateSellerAccountAfterUpdateBySellerId(
                1L, BigDecimal.valueOf(100), BigDecimal.valueOf(200)
        );
        verify(campaignRepository).save(campaign);
        assertThat(result).isNotNull();
    }

    @Test
    void findById_shouldReturnCampaignIfFound() {
        when(campaignRepository.findById(10L)).thenReturn(Optional.of(campaign));

        Campaign result = campainService.findById(10L);

        assertThat(result).isEqualTo(campaign);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(campaignRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> campainService.findById(10L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Campaign not found");
    }

    @Test
    void getAllKeyWordsList_shouldReturnAllKeywords() {
        List<String> result = campainService.getAllKeyWordsList();

        assertThat(result).contains("books", "gaming", "toys");
        assertThat(result).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void getAllTowns_shouldReturnAllEnumValuesAsStrings() {
        List<String> result = campainService.getAllTowns();

        List<String> expected = Arrays.stream(Town.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyElementsOf(expected);
    }
}
