package com.example.ApartmentRenovationCostEstimate.product;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category ASC")
    List<String> findAllDistinctCategories();

    @Query("SELECT DISTINCT p.brand FROM Product p ORDER BY p.brand ASC")
    List<String> findAllDistinctBrands();
}
