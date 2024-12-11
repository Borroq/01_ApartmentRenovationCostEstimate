package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductSaveDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {

    ProductResponseDto createProduct(ProductSaveDto productSaveDto);
    ProductResponseDto getProductById(Long productId);
    Page<ProductResponseDto> getAllProduct(Pageable pageable);
    ProductResponseDto updateProduct(ProductUpdateDto productUpdateDto);
    void deleteProduct(Long productId);
    List<ProductResponseDto> getProductsByCategory(String category);
    List<String> getAllCategories();
    List<String> getAllBrands();
}
