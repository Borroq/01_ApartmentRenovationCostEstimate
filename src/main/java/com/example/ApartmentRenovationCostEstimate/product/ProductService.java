package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductSaveDto;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {

    Product createProduct(ProductSaveDto productSaveDto);
    ProductResponseDto getProductById(Long productId);
    Page<ProductResponseDto> getAllProduct(Pageable pageable);
    Product updateProduct(ProductUpdateDto productUpdateDto);
    void deleteProduct(Long productId);
    List<ProductResponseDto> getProductsByCategory(String category);
    List<String> getAllCategories();
    List<String> getAllBrands();
}
