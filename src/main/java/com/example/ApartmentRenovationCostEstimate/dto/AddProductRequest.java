package com.example.ApartmentRenovationCostEstimate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class AddProductRequest {
    private Long productId;
    private int quantity;
}
