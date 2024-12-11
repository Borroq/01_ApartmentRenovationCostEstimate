package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductSaveDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductUpdateDto;
import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@Valid @RequestBody ProductSaveDto productSaveDto) {
        ProductResponseDto savedProduct = productService.createProduct(productSaveDto);

        return new ResponseEntity<>(new ApiResponse<>("Product created successfully.", savedProduct), HttpStatus.CREATED);
    }


    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable("id") Long productId) {
        ProductResponseDto product = productService.getProductById(productId);

        return new ResponseEntity<>(new ApiResponse<>("Product retrieved successfully", product),HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDto> products = productService.getAllProduct(pageable);

        return new ResponseEntity<>(new ApiResponse<>("Products retrieved successfully", products), HttpStatus.OK);
    }


    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @Valid @PathVariable("id") Long productId,
            @RequestBody ProductUpdateDto productUpdateDto) {

        productUpdateDto.setId(productId);
        ProductResponseDto updateProduct = productService.updateProduct(productUpdateDto);

        return new ResponseEntity<>(new ApiResponse<>("Product updated successfully", updateProduct), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);

        return new ResponseEntity<>(new ApiResponse<>("Product successfully deleted"), HttpStatus.OK);
    }


    @GetMapping("category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDto> productsByCategory = productService.getProductsByCategory(category);

        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }


    @GetMapping("categories")
    public ResponseEntity<ApiResponse<List<String>>> getProductCategories() {
        List<String> categories = productService.getAllCategories();

        return new ResponseEntity<>(new ApiResponse<>("Categories retrieved successfully", categories), HttpStatus.OK);
    }


    @GetMapping("brands")
    public ResponseEntity<ApiResponse<List<String>>> getProductBrands() {
        List<String> brands = productService.getAllBrands();

        return new ResponseEntity<>(new ApiResponse<>("Brands retrieved successfully", brands), HttpStatus.OK);
    }

}
