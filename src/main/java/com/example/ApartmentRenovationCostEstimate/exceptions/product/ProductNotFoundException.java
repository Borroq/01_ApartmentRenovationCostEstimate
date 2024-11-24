package com.example.ApartmentRenovationCostEstimate.exceptions.product;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
