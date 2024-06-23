package com.example.ApartmentRenovationCostEstimate.services;

import com.example.ApartmentRenovationCostEstimate.entity.User;
import com.example.ApartmentRenovationCostEstimate.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImplement implements UserService {
    private UserRepository userRepository;
    @Autowired
    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.get();
    }

    @Override
    public List<User> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(),false)
                .collect(Collectors.toList());
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).get();
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setNick(user.getNick());
        existingUser.setEmail(user.getEmail());
        User updateUser = userRepository.save(existingUser);
        return updateUser;
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
