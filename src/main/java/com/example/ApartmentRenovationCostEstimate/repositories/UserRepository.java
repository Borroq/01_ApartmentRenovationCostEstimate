package com.example.ApartmentRenovationCostEstimate.repositories;

import com.example.ApartmentRenovationCostEstimate.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByNick(String nick);
}
