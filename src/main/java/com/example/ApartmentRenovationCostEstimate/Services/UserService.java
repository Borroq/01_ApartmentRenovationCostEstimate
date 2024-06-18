package com.example.ApartmentRenovationCostEstimate.Services;

import com.example.ApartmentRenovationCostEstimate.entityDomain.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Integer userId);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Integer userId);
}
