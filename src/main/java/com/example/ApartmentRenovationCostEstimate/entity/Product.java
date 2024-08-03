package com.example.ApartmentRenovationCostEstimate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String brand;
    private String category;
    private BigDecimal price;

    public Product(String name, String brand, String category, BigDecimal price) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
    }
}
