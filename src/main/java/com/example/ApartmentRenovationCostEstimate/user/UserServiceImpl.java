package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.Security.GrantedAuthorityImpl;
import com.example.ApartmentRenovationCostEstimate.Security.RoleRepository;
import com.example.ApartmentRenovationCostEstimate.user.DTO.UserSave;
import com.example.ApartmentRenovationCostEstimate.user.DTO.UserUpdate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


    // createUser without ModelMapper
/*    @Override
    public String createUser(UserSave userSave) {
        // Sprawdzenie, czy użytkownik o podanym e-mailu już istnieje
        User user = userRepository.findByEmail(userSave.getEmail()).orElse(null);
        if (user != null) {
            return "Already Exist";
        }
        // Ręczne mapowanie pól z DTO (PersonSaveCO) na encję (Person)
        user = new User();
        user.setName(userSave.getName());
        user.setSurname(userSave.getSurname());
        user.setNick(userSave.getNick());
        user.setEmail(userSave.getEmail());
        // Szyfrowanie hasła
        user.setPassword(passwordEncoder.encode(userSave.getPassword()));

        // Ustawienie ról (GrantedAuthority)
        GrantedAuthorityImpl grantedAuthority = new GrantedAuthorityImpl();
        grantedAuthority.setAuthority(userSave.getRole());
        user.setGratedAuthorities(Collections.singletonList(grantedAuthority));

        // Zapis osoby do bazy danych
        userRepository.save(user);
        return "User Saved";
    }*/


    // create User with ModelMapper
    @Override
    public String createUser(UserSave userSave) {
        User user = userRepository.findByEmail(userSave.getEmail()).orElse(null);
        if (user != null) {
            return "User already exist";
        }

        // user mapping
        user = modelMapper.map(userSave, User.class);

        String role;
        if (userSave.getRole() != null) {
            role = userSave.getRole();
        } else {
            role = "ROLE_USER";
        }

        // Setting roles (GrantedAuthority)
        GrantedAuthorityImpl grantedAuthority = roleRepository.findByAuthority(role)
                .orElseGet(() -> {
            GrantedAuthorityImpl newRole = new GrantedAuthorityImpl();
            newRole.setAuthority(role);
            return roleRepository.save(newRole);
        });

        user.setGrantedAuthorities(Collections.singletonList(grantedAuthority));

        // Password encryption
        user.setPassword(passwordEncoder.encode(userSave.getPassword()));

        // Saving to the database
        userRepository.save(user);
        return "User Saved";
    }


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }


    @Override
    public List<User> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(),false)
                .collect(Collectors.toList());
    }


    // updateUser without ModelMapper
/*    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).get();
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setNick(user.getNick());
        existingUser.setEmail(user.getEmail());
        User updateUser = userRepository.save(existingUser);
        return updateUser;
    }    */


    // createUser with ModelMapper
    @Override
    public String updateUser(UserUpdate userUpdate) {
        User existingUser = userRepository.findById(userUpdate.getId()).orElse(null);
        if (existingUser == null) {
            return "User does not exist";
        }

        if(userUpdate.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        }

        modelMapper.map(userUpdate, existingUser);
        userRepository.save(existingUser);

        return "User updated";
    }


    //@Transactional    // Dzięki adnotacji @Transactional operacje na bazie danych będą wykonane w jednej transakcji.
                        // Adnotacja @Transactional zapewnia, że jeśli cokolwiek pójdzie nie tak, wszystkie zmiany zostaną wycofane.
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
}
