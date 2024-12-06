package com.example.ApartmentRenovationCostEstimate.cart.DTOs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCartDTO {

    @NotNull(message = "UserId is required")
    @Positive(message = "UserId must be greater than 0")
    Long userId;

    @NotBlank(message = "Cart name is required")
    String name;
}
