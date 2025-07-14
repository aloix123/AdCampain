package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    public Product(){}
    public Product(String name, Seller seller) {
        this.name = name;
        this.seller = seller;
    }
    public Product(Long id, String name, Seller seller) {
        this.id = id;
        this.name = name;
        this.seller = seller;
    }


    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
