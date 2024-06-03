package com.example.ApartmentRenovationCostEstimate.repositories;

import com.example.ApartmentRenovationCostEstimate.entityDomain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
