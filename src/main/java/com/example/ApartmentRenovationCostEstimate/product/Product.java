package com.example.ApartmentRenovationCostEstimate.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String brand;
    String link;
    String category;
    BigDecimal price;

    public Product(String name, String brand, String link, String category, BigDecimal price) {
        this.name = name;
        this.brand = brand;
        this.link = link;
        this.category = category;
        this.price = price;
    }
}
