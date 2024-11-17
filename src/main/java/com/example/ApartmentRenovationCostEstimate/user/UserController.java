package com.example.ApartmentRenovationCostEstimate.user;


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
    public ResponseEntity<String> createUser(@RequestBody UserSaveDto userSaveDto){
        String savedUser = userService.createUser(userSaveDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    //Get User by ID - REST API
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId){
        User user = userService.getUserById(userId);
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
    public ResponseEntity<String> updateUser(@PathVariable("id") Long userId, @RequestBody UserUpdateDto userUpdateDto){
        userUpdateDto.setId(userId);
        String uptadeUser = userService.updateUser(userUpdateDto);
        return new ResponseEntity<>(uptadeUser,HttpStatus.OK);
    }

    //Delete User by Id
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User succesfully deleted", HttpStatus.OK);
    }
}
