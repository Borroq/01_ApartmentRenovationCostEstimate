package com.example.ApartmentRenovationCostEstimate.services;

import com.example.ApartmentRenovationCostEstimate.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(Integer productId);
    List<Product> getAllProduct();
    Product updateProduct(Product product);
    void deleteProduct(Integer productId);
}
