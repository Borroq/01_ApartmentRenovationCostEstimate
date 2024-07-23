package com.example.ApartmentRenovationCostEstimate.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;

}
