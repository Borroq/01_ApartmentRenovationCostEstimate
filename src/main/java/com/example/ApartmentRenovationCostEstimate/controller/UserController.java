package com.example.ApartmentRenovationCostEstimate.controller;

import com.example.ApartmentRenovationCostEstimate.services.UserService;
import com.example.ApartmentRenovationCostEstimate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:8080/api/user/...
@RestController
@RequestMapping("api/users")
public class UserController{
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Create User - REST API
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    //Get User by ID - REST API
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer userId){
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    //Get all Users - REST API
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    //Update User by Id - REST API
    @PutMapping({"{id}"})
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer userId, @RequestBody User user){
        user.setId(userId);
        User uptadeUser = userService.updateUser(user);
        return new ResponseEntity<>(uptadeUser,HttpStatus.OK);
    }

    //Delete User by Id
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Integer userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User succesfully deleted", HttpStatus.OK);
    }
}
