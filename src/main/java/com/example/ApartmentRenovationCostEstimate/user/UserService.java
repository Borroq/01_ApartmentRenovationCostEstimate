package com.example.ApartmentRenovationCostEstimate.user;

import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserResponseDto;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserSaveDto;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserUpdateDto;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User createUser(UserSaveDto userSaveDto);
    UserResponseDto getUserById(Long userId);
    List<UserResponseDto> getAllUsers();
    User updateUser(UserUpdateDto userUpdateDto);
    void deleteUser(Long userId);
    Optional<User> findById(Long id);
}
