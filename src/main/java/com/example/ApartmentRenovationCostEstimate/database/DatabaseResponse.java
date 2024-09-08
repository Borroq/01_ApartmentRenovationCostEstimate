package com.example.ApartmentRenovationCostEstimate.database;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class DatabaseResponse {
    private String status;
    private String message;

    public DatabaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
