package com.example.ApartmentRenovationCostEstimate.exceptions;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(String message) {
        super(message);
    }
}
