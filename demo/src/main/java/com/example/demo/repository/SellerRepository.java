package com.example.demo.repository;

import com.example.demo.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {
    @Query("SELECT s FROM Seller s WHERE s.id = :id")
    Optional<Seller> getSellerById(@Param("id") Long id);
    @Query("select s from Seller s")
    List<Seller> getAll();
}
