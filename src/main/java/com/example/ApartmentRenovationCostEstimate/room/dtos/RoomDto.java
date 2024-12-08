package com.example.ApartmentRenovationCostEstimate.room.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDto {

    Long id;

    @NotNull(message = "Name cannot be null")
    String name;

    @NotNull(message = "Floor area cannot be null")
    @Positive(message = "Floor area must be greater than 0")
    Double floorArea;

    @NotNull(message = "Wall area cannot be null")
    @Positive(message = "Wall area must be greater than 0")
    Double wallArea;
}
