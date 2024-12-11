package com.example.ApartmentRenovationCostEstimate.exceptions.product;

public class ProductCategoryNotFoundException extends RuntimeException{
    public ProductCategoryNotFoundException(String message) {
        super(message);
    }
}
