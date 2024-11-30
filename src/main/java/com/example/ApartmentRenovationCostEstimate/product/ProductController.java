package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductSaveDto;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductUpdateDto;
import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    //Create Product - REST API
    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductSaveDto productSaveDto){
        Product savedProduct = productService.createProduct(productSaveDto);

        return new ResponseEntity<>(new ApiResponse<>("Product created successfully.", savedProduct), HttpStatus.CREATED);
    }

    //Get Product by ID - REST API
    @GetMapping("{id}")
    public ResponseEntity<Object> getProductById(@PathVariable("id") Long productId){
        ProductResponseDto product = productService.getProductById(productId);

        return new ResponseEntity<>(new ApiResponse<>("Product retrieved successfully", product),HttpStatus.OK);
    }

    //Get all Products - REST API
    @GetMapping
    public ResponseEntity<Object> getAllProducts(){
        List<ProductResponseDto> products = productService.getAllProduct();
        if (products.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>("No products found."), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse<>("Products retrieved successfully", products), HttpStatus.OK);
    }

    //Update Product by Id - REST API
    @PutMapping("{id}")
    public ResponseEntity<Object> updateProduct(@Valid @PathVariable("id") Long productId, @RequestBody ProductUpdateDto productUpdateDto) {

        productUpdateDto.setId(productId);
        Product updateProduct = productService.updateProduct(productUpdateDto);

        return new ResponseEntity<>(new ApiResponse<>("Product updated successfully", updateProduct), HttpStatus.OK);
    }

    //Delete Product by Id
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);

        return new ResponseEntity<>(new ApiResponse<>("Product successfully deleted"), HttpStatus.OK);
    }

    @GetMapping("category/{category}")
    public ResponseEntity<Object> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDto> productsByCategory = productService.getProductsByCategory(category);
        if (productsByCategory.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>("No products found in the category: " + category), HttpStatus.OK);
        }

        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }

    @GetMapping("categories")
    public ResponseEntity<Object> getProductCategories() {
        List<String> categories = productService.getAllCategories();

        return new ResponseEntity<>(new ApiResponse<>("Categories retrieved successfully", categories), HttpStatus.OK);
    }

    @GetMapping("brands")
    public ResponseEntity<Object> getProductBrands() {
        List<String> brands = productService.getAllBrands();

        return new ResponseEntity<>(new ApiResponse<>("Brands retrieved successfully", brands), HttpStatus.OK);
    }

}
