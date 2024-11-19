package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorType;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserSaveDto;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserUpdateDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<Object> createUser(@RequestBody UserSaveDto userSaveDto){
        User savedUser = userService.createUser(userSaveDto);

        return new ResponseEntity<>(new ApiResponse<>("User created successfully.", savedUser), HttpStatus.CREATED);
    }


    //Get User by ID - REST API
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long userId){
        Optional<User> user = userService.findById(userId);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    //Get all Users - REST API
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();

        return new ResponseEntity<>(users,HttpStatus.OK);
    }


    //Update User by Id - REST API
    @PutMapping("{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long userId, @RequestBody UserUpdateDto userUpdateDto){
        userUpdateDto.setId(userId);
        User uptadeUser = userService.updateUser(userUpdateDto);

        return new ResponseEntity<>(new ApiResponse<>("User updated successfully" , uptadeUser),HttpStatus.OK);
    }


    //Delete User by Id
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User succesfully deleted", HttpStatus.OK);
    }
}
