package com.example.ApartmentRenovationCostEstimate.user.dtos;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSummaryDto {
    Long id;
    String name;
    String surname;
    String email;
}
