package com.example.ApartmentRenovationCostEstimate.entityDomain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String brand;
    private String category;
    private double price;

    public Product() {
    }
    public Product(String name, String brand, String category, double price) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
    }
}
