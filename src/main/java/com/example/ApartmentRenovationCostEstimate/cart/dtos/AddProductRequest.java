package com.example.ApartmentRenovationCostEstimate.cart.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter @Setter  @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddProductRequest {

    @NotNull(message = "Product ID cannot be null")
    @PositiveOrZero(message = "Quantity must be greater than zero or equal")
    Long productId;

    //@Positive(message = "Quantity must be greater than zero")
    int quantity;
}
