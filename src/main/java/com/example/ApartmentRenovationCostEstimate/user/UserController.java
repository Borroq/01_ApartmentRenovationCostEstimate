package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserResponseDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserSaveDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserUpdateDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;



//http://localhost:8080/api/user/...
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("api/users")
public class UserController{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody UserSaveDto userSaveDto){
        UserResponseDto savedUser = userService.createUser(userSaveDto);

        return new ResponseEntity<>(new ApiResponse<>("User created successfully.", savedUser), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable("id") Long userId){
        UserResponseDto user = userService.getUserById(userId);

        return new ResponseEntity<>(new ApiResponse<>("User retrieved successfully", user),HttpStatus.OK);
    }


    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDto> users = userService.getAllUsers(pageable);


        return new ResponseEntity<>(new ApiResponse<>("Users retrieved successfully", users),HttpStatus.OK);
    }


    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @Valid @PathVariable("id") Long userId,
            @RequestBody UserUpdateDto userUpdateDto){

        userUpdateDto.setId(userId);
        UserResponseDto uptadeUser = userService.updateUser(userUpdateDto);

        return new ResponseEntity<>(new ApiResponse<>("User updated successfully" , uptadeUser),HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse<>("User successfully deleted"), HttpStatus.OK);
    }
}
