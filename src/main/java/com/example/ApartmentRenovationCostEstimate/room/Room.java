package com.example.ApartmentRenovationCostEstimate.room;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    Long id;

    @NotBlank(message = "Name cannot be null")
    String name;

    @NotNull(message = "Floor area cannot be null")
    @Positive(message = "Floor area must be greater than 0")
    Double floorArea;

    @NotNull(message = "Wall area cannot be null")
    @Positive(message = "Wall area must be greater than 0")
    Double wallArea;

    public Room(String name, double floorArea, double wallArea) {
        this.name = name;
        this.floorArea = floorArea;
        this.wallArea = wallArea;
    }
}
