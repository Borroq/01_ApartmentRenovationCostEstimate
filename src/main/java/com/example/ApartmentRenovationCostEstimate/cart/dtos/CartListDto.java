package com.example.ApartmentRenovationCostEstimate.cart.dtos;

import com.example.ApartmentRenovationCostEstimate.user.dtos.UserSummaryDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Getter @Setter  @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartListDto {

    Long id;
    String name;
    BigDecimal totalCost;
    int cartItemsCount;
    UserSummaryDto user;

}
