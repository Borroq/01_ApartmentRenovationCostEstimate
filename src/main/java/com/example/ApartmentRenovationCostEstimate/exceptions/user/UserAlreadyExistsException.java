package com.example.ApartmentRenovationCostEstimate.exceptions.user;


public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
