package com.example.demo.service;

import com.example.demo.dto.SellerDTO;
import com.example.demo.exception.NoMoneyException;
import com.example.demo.mapper.SellerMapper;
import com.example.demo.model.Seller;
import com.example.demo.repository.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    public List<SellerDTO> getAll() {
         return SellerMapper.toDTOs(sellerRepository.getAll());
    }

    public SellerDTO getById(Long id) {
        return SellerMapper.toDTO(
                sellerRepository.getSellerById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Seller not found with id: " + id))
        );
    }

    public BigDecimal increaseAccountValue(Long id) {
        BigDecimal defaultMoneyIncrease=BigDecimal.valueOf(1000);
        Seller seller=sellerRepository.getSellerById(id).orElseThrow();
        seller.setEmeraldBalance(seller.getEmeraldBalance().add(defaultMoneyIncrease));
        sellerRepository.save(seller);
        return  seller.getEmeraldBalance();
    }

    public void updateSellerAccountAfterUpdateBySellerId(Long sellerId, BigDecimal oldFund, BigDecimal newFund) {
        Seller seller = sellerRepository.getSellerById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        System.out.println("oldFund"+ oldFund);
        System.out.println("new fund"+ newFund);

        BigDecimal delta = newFund.subtract(oldFund);
        System.out.println("delta"+delta);
        BigDecimal newBalance = seller.getEmeraldBalance().subtract(delta);
        System.out.println("new balance"+newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NoMoneyException("Insufficient funds for campaign update");
        }
        System.out.println(" data has been updates!!!"+newBalance);
        seller.setEmeraldBalance(newBalance);
        sellerRepository.save(seller);
    }

}
