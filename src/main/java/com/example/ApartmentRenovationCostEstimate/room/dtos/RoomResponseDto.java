package com.example.ApartmentRenovationCostEstimate.room.dtos;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponseDto {

    Long id;
    String name;
    Double floorArea;
    Double wallArea;
}
