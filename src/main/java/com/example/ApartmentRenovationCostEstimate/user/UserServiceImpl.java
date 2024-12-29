package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.security.GrantedAuthorityImpl;
import com.example.ApartmentRenovationCostEstimate.security.RoleRepository;
import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserAlreadyExistsException;
import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserNotFoundException;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserResponseDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserSaveDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserUpdateDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional
    public UserResponseDto createUser(UserSaveDto userSaveDto) {
        User user = userRepository.findByEmail(userSaveDto.getEmail()).orElse(null);
        if (user != null) {
            throw new UserAlreadyExistsException("User already exist with email: " + userSaveDto.getEmail());
        }

        // user mapping
        user = modelMapper.map(userSaveDto, User.class);

        String role;
        if (userSaveDto.getRole() != null) {
            role = userSaveDto.getRole();
        } else {
            role = "ROLE_USER";
        }

        // Setting roles (GrantedAuthority)
        GrantedAuthorityImpl grantedAuthority = getOrCreateRole(role);

        user.setGrantedAuthorities(Collections.singletonList(grantedAuthority));

        // Password encryption
        user.setPassword(passwordEncoder.encode(userSaveDto.getPassword()));

        // Saving to the database
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponseDto.class);
    }


    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return modelMapper.map(user, UserResponseDto.class);
    }


    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);

        if (usersPage.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return usersPage
                .map(user -> modelMapper.map(user, UserResponseDto.class));
    }


    @Override
    @Transactional
    public UserResponseDto updateUser(UserUpdateDto userUpdateDto) {
        User existingUser = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(userUpdateDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        modelMapper.map(userUpdateDto, existingUser);

        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserResponseDto.class);
    }


    //@Transactional    // Dzięki adnotacji @Transactional operacje na bazie danych będą wykonane w jednej transakcji.
                        // Adnotacja @Transactional zapewnia, że jeśli cokolwiek pójdzie nie tak, wszystkie zmiany zostaną wycofane.
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Usuń powiązania ról z użytkownikiem, aby uniknąć problemów z kluczami obcymi
        user.getGrantedAuthorities().clear();

        // Zapisz zmiany przed usunięciem użytkownika
        userRepository.save(user);

        userRepository.deleteById(userId);
    }


    private GrantedAuthorityImpl getOrCreateRole(String role) {
        return roleRepository.findByAuthority(role)
                .orElseGet(() -> {
                    GrantedAuthorityImpl newRole = new GrantedAuthorityImpl();
                    newRole.setAuthority(role);
                    return roleRepository.save(newRole);
                });
    }

}
