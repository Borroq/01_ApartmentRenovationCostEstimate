package com.example.ApartmentRenovationCostEstimate.services;

import com.example.ApartmentRenovationCostEstimate.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Integer userId);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Integer userId);
}
