package com.example.ApartmentRenovationCostEstimate.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

/*    public enum ErrorType {
        USER_NOT_FOUND("User not found!"),
        USERID_IS_MISSING("UserId is missing!"),
        INVALID_USER_FORMAT("Invalid user format!"),
        CART_NOT_FOUND("Cart not found!"),
        CART_NAME_IS_MISSING("Cart name is missing!"),
        PRODUCT_NOT_FOUND("Product not found!"),
        
        CART_OR_PRODUCT_NOT_FOUND("Cart or product not found!");

        private String errorMessage;

        ErrorType(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public String toString() {
            return this.errorMessage;
        }
    }*/

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
