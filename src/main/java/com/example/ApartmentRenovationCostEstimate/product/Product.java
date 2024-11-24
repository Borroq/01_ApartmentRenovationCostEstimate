package com.example.ApartmentRenovationCostEstimate.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

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

    @NotBlank(message = "Name cannot be null")
    @Size(min = 3, max = 200)
    String name;

    @NotBlank(message = "Brand cannot be null")
    @Size(min = 3, max = 70)
    String brand;

    @URL(message = "Invalid URL")
    String link;

    @NotBlank(message = "Category cannot be null")
    @Size(min = 3, max = 70)
    String category;

    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    BigDecimal price;

    public Product(String name, String brand, String link, String category, BigDecimal price) {
        this.name = name;
        this.brand = brand;
        this.link = link;
        this.category = category;
        this.price = price;
    }
}
