package com.example.demo.repository;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Optional<Product> findByName(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId")
    List<Product> findBySellerId(@Param("sellerId") Long sellerId);

}
