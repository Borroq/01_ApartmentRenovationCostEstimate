package com.example.ApartmentRenovationCostEstimate.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String email;
    private String password;
}
