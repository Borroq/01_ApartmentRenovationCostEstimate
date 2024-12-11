package com.example.ApartmentRenovationCostEstimate.exceptions;


import com.example.ApartmentRenovationCostEstimate.exceptions.cart.CartItemNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.cart.CartNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductCategoryNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.room.RoomNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserAlreadyExistsException;
import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserNotFoundException;
import com.example.ApartmentRenovationCostEstimate.response.ErrorResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException (UserNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.USER_NOT_FOUND), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException (UserAlreadyExistsException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.USER_ALREADY_EXIST),HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException (ProductNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (ProductCategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductCategoryNotFoundException (ProductCategoryNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException (CartNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.CART_NOT_FOUND), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartItemNotFoundException (CartItemNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFoundException (RoomNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.ROOM_NOT_FOUND), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions (MethodArgumentNotValidException exception) {
        List<Map<String, String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResponse(ErrorType.VALIDATION_ERROR, errors), HttpStatus.BAD_REQUEST);
    }

}
