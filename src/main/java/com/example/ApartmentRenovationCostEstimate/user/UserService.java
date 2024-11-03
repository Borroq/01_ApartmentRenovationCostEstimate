package com.example.ApartmentRenovationCostEstimate.user;

import com.example.ApartmentRenovationCostEstimate.user.DTO.UserSave;
import com.example.ApartmentRenovationCostEstimate.user.DTO.UserUpdate;

import java.util.List;
import java.util.Optional;


public interface UserService {
    String createUser(UserSave userSave);
    User getUserById(Long userId);
    List<User> getAllUsers();
    String updateUser(UserUpdate userUpdate);
    void deleteUser(Long userId);
    Optional<User> findById(Long id);
}
