package com.example.ApartmentRenovationCostEstimate.exceptions;


import com.example.ApartmentRenovationCostEstimate.response.ErrorResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException (UserNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.USER_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException (ProductNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException (CartNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.CART_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFoundException (RoomNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(ErrorType.ROOM_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

}
