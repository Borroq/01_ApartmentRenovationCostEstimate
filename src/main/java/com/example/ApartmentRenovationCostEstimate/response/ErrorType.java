package com.example.ApartmentRenovationCostEstimate.response;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorType {
    USER_NOT_FOUND("User not found!"),
    USERID_IS_MISSING("UserId is missing!"),
    USER_ALREADY_EXIST("User already exist!"),
    INVALID_USER_FORMAT("Invalid user format!"),
    CART_NOT_FOUND("Cart not found!"),
    CART_NAME_IS_MISSING("Cart name is missing!"),
    PRODUCT_NOT_FOUND("Product not found!"),
    FAILED_TO_ADD_PRODUCT_TO_CART("Failed to add product to cart!"),
    ROOM_NOT_FOUND("Room not found!"),
    ROOMS_NOT_FOUND("Rooms not found!"),

    VALIDATION_ERROR("Validation failed!"),

    CART_OR_PRODUCT_NOT_FOUND("Cart or product not found!");


    private String errorMessage;

    ErrorType(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
