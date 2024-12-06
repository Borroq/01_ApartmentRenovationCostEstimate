package com.example.ApartmentRenovationCostEstimate.cart.DTOs;

import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserSummaryDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartListDto {

    Long id;
    String name;
    BigDecimal totalCost;
    int cartItemsCount;
    UserSummaryDto user;

}
