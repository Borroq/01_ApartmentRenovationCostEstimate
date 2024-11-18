package com.example.ApartmentRenovationCostEstimate.exceptions;

public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException(String message) {
        super(message);
    }
}
