package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.user.dtos.UserResponseDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserSaveDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface UserService {
    UserResponseDto createUser(UserSaveDto userSaveDto);
    UserResponseDto getUserById(Long userId);
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto updateUser(UserUpdateDto userUpdateDto);
    void deleteUser(Long userId);
}
