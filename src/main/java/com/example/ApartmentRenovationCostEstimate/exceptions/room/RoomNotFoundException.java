package com.example.ApartmentRenovationCostEstimate.exceptions.room;

public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException(String message) {
        super(message);
    }
}
