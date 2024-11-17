package com.example.ApartmentRenovationCostEstimate.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException() {
        super("Product not found");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
