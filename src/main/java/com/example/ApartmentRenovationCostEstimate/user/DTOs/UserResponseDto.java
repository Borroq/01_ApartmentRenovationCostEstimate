package com.example.ApartmentRenovationCostEstimate.user.DTOs;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {

    Long id;
    String name;
    String surname;
    String nick;
    String email;
    String role;
}
