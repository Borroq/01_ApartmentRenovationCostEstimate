package com.example.ApartmentRenovationCostEstimate.cart.DTOs;

import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemDto {
    private Long id;
    private ProductResponseDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
