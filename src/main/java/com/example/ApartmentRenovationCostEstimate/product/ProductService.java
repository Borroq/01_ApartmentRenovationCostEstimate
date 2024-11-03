package com.example.ApartmentRenovationCostEstimate.product;

import java.util.List;


public interface ProductService {

    Product createProduct(Product product);
    Product getProductById(Long productId);
    List<Product> getAllProduct();
    Product updateProduct(Product product);
    void deleteProduct(Long productId);
    List<Product> getProductsByCategory(String category);
    List<String> getAllCategories();
    List<String> getAllBrands();
}
