package com.example.ApartmentRenovationCostEstimate;

import com.example.ApartmentRenovationCostEstimate.entityDomain.Product;
import com.example.ApartmentRenovationCostEstimate.entityDomain.User;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(Integer productId);
    List<Product> getAllProduct();
    Product updateProduct(Product product);
    void deleteProduct(Integer productId);
}
