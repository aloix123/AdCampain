package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    Product(){}

    public Product(String name, Seller seller) {
        this.name = name;
        this.seller = seller;
    }

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
