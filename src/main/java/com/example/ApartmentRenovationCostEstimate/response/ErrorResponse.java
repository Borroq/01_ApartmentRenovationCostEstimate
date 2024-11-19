package com.example.ApartmentRenovationCostEstimate.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Optional<String> message = Optional.empty();
    private ErrorType type;

    public ErrorResponse(ErrorType type) {
        this.type = type;
    }

    public ErrorResponse(ErrorType type, String message) {
        this.type = type;
        this.message = Optional.of(message);
    }

    public String getMessage() {
        if (message.isPresent()) {
            return type.getErrorMessage() + " " + message.get();
        }
        return type.getErrorMessage();
    }


}
