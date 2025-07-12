package com.example.demo.repository;

import com.example.demo.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign,Long> {
    @Query("SELECT c FROM Campaign c WHERE c.product.seller.id = :sellerId")
    List<Campaign> getAllBySellerId(@Param("sellerId") Long sellerId);


}
