package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductNotFoundException;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductSaveDto;
import com.example.ApartmentRenovationCostEstimate.product.DTOs.ProductUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Product createProduct(ProductSaveDto productSaveDto) {
        Product product = modelMapper.map(productSaveDto, Product.class);

        return productRepository.save(product);
    }

    @Override
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return modelMapper.map(product, ProductResponseDto.class);
    }


    @Override
    public Page<ProductResponseDto> getAllProduct(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    @Override
    @Transactional
    public Product updateProduct(ProductUpdateDto productUpdateDto) {
        Product existingProduct = productRepository.findById(productUpdateDto.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        modelMapper.map(productUpdateDto, existingProduct);

        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(String category) {
        List<Product> productByCategory = productRepository.findByCategory(category);

        return productByCategory.stream()
                .map(productCategory -> modelMapper.map(productCategory, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCategories() {
        List<Product> products = (List<Product>) productRepository.findAll();

        return products.stream()
                .map(Product::getCategory)
                .distinct() //usuwanie duplikatów
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllBrands() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return products.stream()
                .map(Product::getBrand)
                .distinct() //usuwanie duplikatów
                .collect(Collectors.toList());
    }
}
