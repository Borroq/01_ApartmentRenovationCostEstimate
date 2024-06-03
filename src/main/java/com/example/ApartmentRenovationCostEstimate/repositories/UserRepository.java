package com.example.ApartmentRenovationCostEstimate.repositories;

import com.example.ApartmentRenovationCostEstimate.entityDomain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
