package com.example.ApartmentRenovationCostEstimate.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Optional<String> message = Optional.empty();
    private ErrorType type;
    private List<Map<String, String>> errors;

    public ErrorResponse(ErrorType type) {
        this.type = type;
    }


    public ErrorResponse(ErrorType type, String message) {
        this.type = type;
        this.message = Optional.of(message);
    }


    public ErrorResponse(ErrorType type, List<Map<String, String>> errors) {
        this.type = type;
        this.errors = errors;
    }


    public String getMessage() {
        if (message.isPresent()) {
            return type.getErrorMessage() + " " + message.get();
        }
        return type.getErrorMessage();
    }
}
