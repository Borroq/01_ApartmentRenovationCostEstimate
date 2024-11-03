package com.example.ApartmentRenovationCostEstimate.user.DTO;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSave {

    String name;
    String surname;
    String nick;
    String password;
    String email;
    String role;
}
