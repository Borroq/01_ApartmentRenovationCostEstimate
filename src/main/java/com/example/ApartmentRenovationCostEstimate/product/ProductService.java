package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductSaveDto;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductUpdateDto;

import java.util.List;


public interface ProductService {

    Product createProduct(ProductSaveDto productSaveDto);
    ProductResponseDto getProductById(Long productId);
    List<ProductResponseDto> getAllProduct();
    Product updateProduct(ProductUpdateDto productUpdateDto);
    void deleteProduct(Long productId);
    List<ProductResponseDto> getProductsByCategory(String category);
    List<String> getAllCategories();
    List<String> getAllBrands();
}
