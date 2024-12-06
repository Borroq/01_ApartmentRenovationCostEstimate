package com.example.ApartmentRenovationCostEstimate.exceptions.cart;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
