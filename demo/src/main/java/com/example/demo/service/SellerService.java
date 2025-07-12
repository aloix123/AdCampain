package com.example.demo.service;

import com.example.demo.exception.NoMoneyException;
import com.example.demo.model.Seller;
import com.example.demo.repository.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class SellerService {
    private SellerRepository sellerRepository;

    public void updateSellerAccountBySellerId(Long id, BigDecimal value) {
        Seller seller = sellerRepository.getSellerById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        BigDecimal currentBalance = seller.getEmeraldBalance();
        BigDecimal newBalance = currentBalance.subtract(value); // value to BigDecimal
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NoMoneyException("Insufficient funds");
        }
        seller.setEmeraldBalance(newBalance);
        sellerRepository.save(seller);
    }

}
