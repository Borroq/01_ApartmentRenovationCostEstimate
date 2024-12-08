package com.example.ApartmentRenovationCostEstimate.user.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDto {

    Long id;

    @NotBlank(message = "Name cannot be null")
    @Size(min = 3, max = 70)
    String name;

    @NotBlank(message = "Surname cannot be null")
    @Size(min = 3, max = 70)
    String surname;

    @Size(max = 70)
    String nick;

    @Size(min = 6, max = 200)
    String password;

    @Email(message = "Invalid email format")
    String email;
    String role;
}
