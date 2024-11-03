package com.example.ApartmentRenovationCostEstimate.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;
    double floorArea;
    double wallArea;

    public Room(String name, double floorArea, double wallArea) {
        this.name = name;
        this.floorArea = floorArea;
        this.wallArea = wallArea;
    }
}
