package com.example.ApartmentRenovationCostEstimate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Room {
    private String name;
    private double floorArea;
    private double wallArea;

    public Room() {
    }
    public Room(String name, double floorArea, double wallArea) {
        this.name = name;
        this.floorArea = floorArea;
        this.wallArea = wallArea;
    }
}
