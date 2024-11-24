package com.example.ApartmentRenovationCostEstimate.exceptions.cart;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(String message) {
        super(message);
    }
}
