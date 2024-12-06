package com.example.ApartmentRenovationCostEstimate.cart.DTOs;


import com.example.ApartmentRenovationCostEstimate.shared.dtos.PageMetadata;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserSummaryDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponseDto {

    Long id;
    String name;
    BigDecimal totalCost;
    UserSummaryDto user;
    List<CartItemDto> cartItems;
    PageMetadata cartItemsPageMetadata;
}
