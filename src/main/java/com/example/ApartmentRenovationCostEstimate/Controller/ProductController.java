package com.example.ApartmentRenovationCostEstimate.Controller;

import com.example.ApartmentRenovationCostEstimate.Services.ProductService;
import com.example.ApartmentRenovationCostEstimate.entityDomain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    //Get Product by ID - REST API
    @GetMapping("{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer productId){
        Product product = productService.getProductById(productId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    //Get all Users - REST API
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //Update User by Id - REST API
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer productId, @RequestBody Product product) {
        product.setId(productId);
        Product updateProduct = productService.updateProduct(product);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }

    //Delete User by Id
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product succesfully deleted", HttpStatus.OK);
    }
}
