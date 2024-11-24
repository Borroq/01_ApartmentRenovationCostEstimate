package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserResponseDto;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserSaveDto;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserUpdateDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    //Create User - REST API
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserSaveDto userSaveDto){
        User savedUser = userService.createUser(userSaveDto);

        return new ResponseEntity<>(new ApiResponse<>("User created successfully.", savedUser), HttpStatus.CREATED);
    }


    //Get User by ID - REST API
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId){
        UserResponseDto user = userService.getUserById(userId);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    //Get all Users - REST API
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> getAllUsers(){
        List<UserResponseDto> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>("No rooms found"), HttpStatus.OK);
        }

        return new ResponseEntity<>(users,HttpStatus.OK);
    }


    //Update User by Id - REST API
    @PutMapping("{id}")
    public ResponseEntity<Object> updateUser(@Valid @PathVariable("id") Long userId, @RequestBody UserUpdateDto userUpdateDto){
        userUpdateDto.setId(userId);
        User uptadeUser = userService.updateUser(userUpdateDto);

        return new ResponseEntity<>(new ApiResponse<>("User updated successfully" , uptadeUser),HttpStatus.OK);
    }


    //Delete User by Id
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse<>("User successfully deleted"), HttpStatus.OK);
    }
}
