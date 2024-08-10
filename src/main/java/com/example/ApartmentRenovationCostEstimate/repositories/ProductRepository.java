package com.example.ApartmentRenovationCostEstimate.repositories;

import com.example.ApartmentRenovationCostEstimate.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByCategory(String category); // W JAKI SPOSOB SPRING WIE ŻE WYWOLUJĄC TĄ METODĘ MA FILTROWAĆ PO DANEJ KATEGORII !!!
}
