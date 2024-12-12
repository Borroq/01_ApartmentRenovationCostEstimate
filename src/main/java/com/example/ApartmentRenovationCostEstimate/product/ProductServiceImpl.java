package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductCategoryNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductNotFoundException;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductSaveDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductUpdateDto;
import org.modelmapper.ModelMapper;
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


    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductSaveDto productSaveDto) {
        Product product = modelMapper.map(productSaveDto, Product.class);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }


    @Override
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return modelMapper.map(product, ProductResponseDto.class);
    }


    @Override
    public Page<ProductResponseDto> getAllProduct(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);

        if (productsPage.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        return productsPage
                .map(product -> modelMapper.map(product, ProductResponseDto.class));
    }


    @Override
    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateDto productUpdateDto) {
        Product existingProduct = productRepository.findById(productUpdateDto.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        existingProduct.setName(productUpdateDto.getName());
        existingProduct.setBrand(productUpdateDto.getBrand());
        existingProduct.setLink(productUpdateDto.getLink());
        existingProduct.setCategory(productUpdateDto.getCategory());
        existingProduct.setPrice(productUpdateDto.getPrice());

        Product updatedProduct = productRepository.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductResponseDto.class);
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

        if (productByCategory.isEmpty()) {
            throw new ProductCategoryNotFoundException("Product category not found");
        }

        return productByCategory.stream()
                .map(productCategory -> modelMapper.map(productCategory, ProductResponseDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<String> getAllCategories() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(Product::getCategory)
                .distinct() //usuwanie duplikatów
                .collect(Collectors.toList());
    }


    @Override
    public List<String> getAllBrands() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(Product::getBrand)
                .distinct()
                .collect(Collectors.toList());
    }
}
