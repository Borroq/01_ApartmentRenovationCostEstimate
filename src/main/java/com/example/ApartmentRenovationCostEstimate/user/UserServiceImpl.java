package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.Security.GrantedAuthorityImpl;
import com.example.ApartmentRenovationCostEstimate.Security.RoleRepository;
import com.example.ApartmentRenovationCostEstimate.exceptions.UserAlreadyExistsException;
import com.example.ApartmentRenovationCostEstimate.exceptions.UserNotFoundException;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserResponseDto;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserSaveDto;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserUpdateDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    // create User with ModelMapper
    @Override
    @Transactional
    public User createUser(UserSaveDto userSaveDto) {
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
        return userRepository.save(user);
    }


    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return modelMapper.map(user, UserResponseDto.class);
    }


    @Override
    public List<UserResponseDto> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(),false)
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }


    // createUser with ModelMapper
    @Override
    @Transactional
    public User updateUser(UserUpdateDto userUpdateDto) {
        User existingUser = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(userUpdateDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        modelMapper.map(userUpdateDto, existingUser);

        return userRepository.save(existingUser);
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


    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
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
