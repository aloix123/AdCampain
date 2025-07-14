package com.example.demo.unitTest;

import com.example.demo.dto.SellerDTO;
import com.example.demo.exception.NoMoneyException;
import com.example.demo.model.Seller;
import com.example.demo.repository.SellerRepository;
import com.example.demo.service.SellerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerServiceUnitTest {

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerService sellerService;

    private Seller testSeller;

    @BeforeEach
    void setUp() {
        testSeller = new Seller();
        testSeller.setId(1L);
        testSeller.setEmeraldBalance(BigDecimal.valueOf(2000));
    }

    @Test
    void updateSellerAccountBySellerId_shouldUpdateBalance() {
        BigDecimal deduction = BigDecimal.valueOf(500);
        when(sellerRepository.getSellerById(1L)).thenReturn(Optional.of(testSeller));

        sellerService.updateSellerAccountBySellerId(1L, deduction);

        assertThat(testSeller.getEmeraldBalance()).isEqualByComparingTo("1500");
        verify(sellerRepository).save(testSeller);
    }

    @Test
    void updateSellerAccountBySellerId_shouldThrowWhenInsufficientFunds() {
        BigDecimal deduction = BigDecimal.valueOf(2500);
        when(sellerRepository.getSellerById(1L)).thenReturn(Optional.of(testSeller));

        assertThatThrownBy(() ->
                sellerService.updateSellerAccountBySellerId(1L, deduction)
        ).isInstanceOf(NoMoneyException.class)
                .hasMessage("you dont have much money to create this campain ");

        verify(sellerRepository, never()).save(any());
    }

    @Test
    void updateSellerAccountBySellerId_shouldThrowWhenSellerNotFound() {
        when(sellerRepository.getSellerById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                sellerService.updateSellerAccountBySellerId(1L, BigDecimal.TEN)
        ).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Seller not found");

        verify(sellerRepository, never()).save(any());
    }

    @Test
    void getAll_shouldReturnListOfSellerDTOs() {
        List<Seller> sellers = List.of(testSeller);
        when(sellerRepository.getAll()).thenReturn(sellers);

        List<SellerDTO> result = sellerService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(testSeller.getId());
    }

    @Test
    void getById_shouldReturnSellerDTO() {
        when(sellerRepository.getSellerById(1L)).thenReturn(Optional.of(testSeller));

        SellerDTO result = sellerService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(sellerRepository.getSellerById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sellerService.getById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Seller not found with id: 1");
    }

    @Test
    void increaseAccountValue_shouldAddDefaultMoney() {
        when(sellerRepository.getSellerById(1L)).thenReturn(Optional.of(testSeller));
        BigDecimal newBalance = sellerService.increaseAccountValue(1L);
        assertThat(newBalance).isEqualByComparingTo("3000");
        verify(sellerRepository).save(testSeller);
    }
}
