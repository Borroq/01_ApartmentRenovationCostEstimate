package com.example.ApartmentRenovationCostEstimate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private double floorArea;
    private double wallArea;

    public Room(String name, double floorArea, double wallArea) {
        this.name = name;
        this.floorArea = floorArea;
        this.wallArea = wallArea;
    }
}
