package com.example.ApartmentRenovationCostEstimate.user;

import com.example.ApartmentRenovationCostEstimate.user.dtos.UserResponseDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserSaveDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserUpdateDto;

import java.util.List;


public interface UserService {
    User createUser(UserSaveDto userSaveDto);
    UserResponseDto getUserById(Long userId);
    List<UserResponseDto> getAllUsers();
    User updateUser(UserUpdateDto userUpdateDto);
    void deleteUser(Long userId);
}
