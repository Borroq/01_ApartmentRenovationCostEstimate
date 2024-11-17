package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    //Create Product - REST API
    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody Product product){
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(new ApiResponse<>("Product created successfully.", savedProduct), HttpStatus.CREATED);
    }

    //Get Product by ID - REST API
    @GetMapping("{id}")
    public ResponseEntity<Object> getProductById(@PathVariable("id") Long productId){
        Product product = productService.getProductById(productId);
        if (product == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse<>("Product retrieved successfully", product),HttpStatus.OK);
    }

    //Get all Products - REST API
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //Update Product by Id - REST API
    @PutMapping("{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") Long productId, @RequestBody Product product) {
        Product existingProduct = productService.getProductById(productId);
        if (existingProduct == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        product.setId(productId);
        Product updateProduct = productService.updateProduct(product);

        return new ResponseEntity<>(new ApiResponse<>("Product updated successfully", updateProduct), HttpStatus.OK);
    }

    //Delete Product by Id
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        productService.deleteProduct(productId);
        return new ResponseEntity<>(new ApiResponse<>("Product successfully deleted"), HttpStatus.OK);
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> productsByCategory = productService.getProductsByCategory(category);
        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }

    @GetMapping("categories")
    public ResponseEntity<List<String>> getProductCategories() {
        List<String> categories = productService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("brands")
    public ResponseEntity<List<String>> getProductBrands() {
        List<String> brands = productService.getAllBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

}
