package com.example.ApartmentRenovationCostEstimate.product;

import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductCategoryNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductNotFoundException;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductSaveDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductUpdateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.PAGE_DEFAULT;
import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.SIZE_DEFAULT;
import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.SORT_CATEGORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;

    private ProductServiceImpl underTest;
    private AutoCloseable autoCloseable;


    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ProductServiceImpl(productRepository, modelMapper);
    }

    @AfterEach
    void afterAll() throws Exception {
        autoCloseable.close();
    }


    @Test
    @DisplayName("It should save new Product")
    void itShouldSaveNewProduct() {
        //Given
        String name = "Test productName";
        String brand = "Test Brand";
        String link = "www.test_productLink.com";
        String category = "TestCategory";
        BigDecimal price = new BigDecimal(999.99);

        ProductSaveDto productSaveDto = new ProductSaveDto(
                name,
                brand,
                link,
                category,
                price
        );

        Product product = new Product();
        product.setId(1L);
        product.setName(name);
        product.setBrand(brand);
        product.setLink(link);
        product.setCategory(category);
        product.setPrice(price);

        ProductResponseDto expectedResponse = new ProductResponseDto();
        expectedResponse.setName(name);
        expectedResponse.setBrand(brand);
        expectedResponse.setLink(link);
        expectedResponse.setCategory(category);
        expectedResponse.setPrice(price);


        //Mocking
        when(modelMapper.map(productSaveDto, Product.class)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(modelMapper.map(product, ProductResponseDto.class)).thenReturn(expectedResponse);

        //When
        ProductResponseDto actualResponse = underTest.createProduct(productSaveDto);

        //Then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(expectedResponse, actualResponse),

                () -> verify(modelMapper, times(1)).map(productSaveDto, Product.class),
                () -> verify(productRepository, times(1)).save(product),
                () -> verify(modelMapper, times(1)).map(product, ProductResponseDto.class)
        );
    }


    @Test
    @DisplayName("It should return ProductResponseDto by given Id if exists")
    void itShouldReturnProductByGivenId() {
        //Given
        Long productId = 1L;
        String name = "Test productName";
        String brand = "Test Brand";
        String link = "www.test_productLink.com";
        String category = "TestCategory";
        BigDecimal price = new BigDecimal(999.99);

        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        product.setBrand(brand);
        product.setLink(link);
        product.setCategory(category);
        product.setPrice(price);

        ProductResponseDto expectedResponse = new ProductResponseDto();
        expectedResponse.setName(name);
        expectedResponse.setBrand(brand);
        expectedResponse.setLink(link);
        expectedResponse.setCategory(category);
        expectedResponse.setPrice(price);

        //Mocking
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductResponseDto.class)).thenReturn(expectedResponse);

        //When
        ProductResponseDto actualResponse = underTest.getProductById(productId);

        //Then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(expectedResponse, actualResponse),

                () -> verify(productRepository, times(1)).findById(productId),
                () -> verify(modelMapper, times(1)).map(product, ProductResponseDto.class)
        );
    }


    @Test
    @DisplayName("It should throw ProductNotFoundException when Product doesn't exists")
    void itShouldThrowProductNotFoundExceptionWhenProductDoesNotExists() {
        //Given
        Long productId = 1L;

        //Mocking
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(ProductNotFoundException.class, () -> underTest.getProductById(productId));

        verify(productRepository, times(1)).findById(productId);
        verify(modelMapper, times(0)).map(any(), any());
    }

    @Test
    @DisplayName("It should return Product list when porducts exist")
    void itShouldReturnProductListWhenProductsExist() {
        //Given
        Product product_1 = new Product(
                1L,
                "productName 1",
                "brand 1",
                "Link 1",
                "category 1",
                new BigDecimal(1.00)
        );

        Product product_2 = new Product(
                2L,
                "productName 2",
                "brand 2",
                "Link 2",
                "category 2",
                new BigDecimal(2.00)
        );

        Product product_3 = new Product(
                3L,
                "productName 3",
                "brand 3",
                "Link 3",
                "category 3",
                new BigDecimal(3.00)
        );

        ProductResponseDto productDto_1 = new ProductResponseDto(
                1L,
                "productName 1",
                "brand 1",
                "Link 1",
                "category 1",
                new BigDecimal(1.00)
        );

        ProductResponseDto productDto_2 = new ProductResponseDto(
                2L,
                "productName 2",
                "brand 2",
                "Link 2",
                "category 2",
                new BigDecimal(2.00)
        );

        ProductResponseDto productDto_3 = new ProductResponseDto(
                3L,
                "productName 3",
                "brand 3",
                "Link 3",
                "category 3",
                new BigDecimal(3.00)
        );

        int page = Integer.parseInt(PAGE_DEFAULT);
        int size = Integer.parseInt(SIZE_DEFAULT);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(SORT_CATEGORY)));
        Page<Product> productPage = new PageImpl<>(List.of(product_1, product_2, product_3), pageable, 1);

        //Mocking
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(modelMapper.map(product_1, ProductResponseDto.class)).thenReturn(productDto_1);
        when(modelMapper.map(product_2, ProductResponseDto.class)).thenReturn(productDto_2);
        when(modelMapper.map(product_3, ProductResponseDto.class)).thenReturn(productDto_3);

        //When
        Page<ProductResponseDto> actualResponse = underTest.getAllProduct(pageable);

        //Then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(3, actualResponse.getContent().size()),
                () -> assertThat(actualResponse.getContent()).containsExactly(productDto_1, productDto_2, productDto_3),
                () -> assertThat(actualResponse.getContent()).allMatch(product -> product instanceof ProductResponseDto),
                () -> verify(productRepository, times(1)).findAll(pageable),
                () -> verify(modelMapper, times(3)).map(any(Product.class), eq(ProductResponseDto.class))
        );
    }


    @Test
    @DisplayName("It should throw ProductNotFoundException when Products doesn't exist")
    void itShouldThrowProductNotFoundExceptionWhenProductsDoesNotExist() {
        int page = Integer.parseInt(PAGE_DEFAULT);
        int size = Integer.parseInt(SIZE_DEFAULT);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(SORT_CATEGORY)));
        Page<Product> productPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        //When & Then
        ProductNotFoundException throwException = assertThrows(ProductNotFoundException.class, () -> underTest.getAllProduct(pageable));

        assertEquals("Product not found", throwException.getMessage());

        verify(productRepository, times(1)).findAll(pageable);
    }


    @Test
    @DisplayName("It should update product by Id")
    void itShouldUpdateProduct() {
        //Given
        Long productId = 1L;

        ProductUpdateDto productUpdateDto = new ProductUpdateDto();
        productUpdateDto.setId(productId);
        productUpdateDto.setName("Test Updated productName");
        productUpdateDto.setBrand("Test Brand");
        productUpdateDto.setLink("www.test_productLink.com");
        productUpdateDto.setCategory("Test Updated Category");
        productUpdateDto.setPrice(new BigDecimal(999.99));

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Test productName");
        existingProduct.setBrand("Test Brand");
        existingProduct.setLink("www.test_productLink.com");
        existingProduct.setCategory("Test Category");
        existingProduct.setPrice(new BigDecimal(999.99));

        existingProduct.setName(productUpdateDto.getName());
        existingProduct.setCategory(productUpdateDto.getCategory());

        ProductResponseDto expectedResponse = new ProductResponseDto();
        expectedResponse.setId(productId);
        expectedResponse.setName("Test Updated productName");
        expectedResponse.setBrand("Test Brand");
        expectedResponse.setLink("www.test_productLink.com");
        expectedResponse.setCategory("Test Updated Category");
        expectedResponse.setPrice(new BigDecimal(999.99));

        //Mocking
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
        when(modelMapper.map(existingProduct, ProductResponseDto.class)).thenReturn(expectedResponse);

        //When
        ProductResponseDto actualResponse = underTest.updateProduct(productUpdateDto);

        //Then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(expectedResponse, actualResponse),

                () -> verify(productRepository, times(1)).findById(productId),
                () -> verify(modelMapper, times(1)).map(existingProduct, ProductResponseDto.class)
        );
    }


    @Test
    @DisplayName("It should throw ProductNotFoundException when product doesn't exist")
    void itShouldThrowExceptionWhenProductNotFound() {
        //Given
        Long productId = 1L;
        ProductUpdateDto productUpdateDto = new ProductUpdateDto();
        productUpdateDto.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> underTest.updateProduct(productUpdateDto));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(0)).save(any());
        verify(modelMapper, times(0)).map(any(), any());
    }


    @Test
    @DisplayName("It should delete product")
    void itShouldDeleteProduct() {
        //Given
        Product product = new Product(
                1L,
                "productName 1",
                "brand 1",
                "Link 1",
                "category 1",
                new BigDecimal(1.00)
        );

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        //When
        underTest.deleteProduct(product.getId());

        //Then
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(1)).deleteById(product.getId());
    }


    @Test
    @DisplayName("It should return all products of the given category")
    void itShouldReturnProductListForGivenCategory() {
        //Given
        String category = "Test category";
        Product product1 = new Product(
                1L,
                "productName 1",
                "brand 1",
                "Link 1",
                category,
                new BigDecimal(1.00)
        );

        Product product2 = new Product(
                2L,
                "productName 2",
                "brand 2",
                "Link 2",
                category,
                new BigDecimal(2.00)
        );

        List<Product> products = List.of(product1, product2);

        ProductResponseDto productDto1 = new ProductResponseDto(
                1L,
                "productName 1",
                "brand 1",
                "Link 1",
                category,
                new BigDecimal(1.00)
        );

        ProductResponseDto productDto2 = new ProductResponseDto(
                2L,
                "productName 2",
                "brand 2",
                "Link 2",
                category,
                new BigDecimal(2.00)
        );

        //Mocking
        when(productRepository.findByCategory(category)).thenReturn(products);
        when(modelMapper.map(product1, ProductResponseDto.class)).thenReturn(productDto1);
        when(modelMapper.map(product2, ProductResponseDto.class)).thenReturn(productDto2);

        //When
        List<ProductResponseDto> actualResponse = underTest.getProductsByCategory(category);

        //Then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(2, actualResponse.size()),
                () -> assertEquals(productDto1, actualResponse.get(0)),
                () -> assertEquals(productDto2, actualResponse.get(1)),

                () -> verify(productRepository, times(1)).findByCategory(category),
                () -> verify(modelMapper, times(2)).map(any(), any())
        );
    }


    @Test
    @DisplayName("It should return Exception when products not found by given category")
    void itThrowExceptionWhenProductNotFoundByGivenCategory() {
        //Given
        String category = "Non-exception category";
        List<Product> emptyProductList = List.of();

        when(productRepository.findByCategory(category)).thenReturn(emptyProductList);

        ProductCategoryNotFoundException exception = assertThrows(ProductCategoryNotFoundException.class,
                () -> underTest.getProductsByCategory(category));

        assertEquals("Product category not found!", exception.getMessage());

        verify(productRepository, times(1)).findByCategory(category);
    }


    @Test
    @DisplayName("It should return categories list from Products list")
    void itShouldReturnCategoriesList() {
        //Given
        List<String> categories = List.of("Category 1", "Category 2", "Category 3");

        //Mocking
        when(productRepository.findAllDistinctCategories()).thenReturn(categories);

        List<String> actualCategories = underTest.getAllCategories();

        //Then
        assertAll(
                () -> assertNotNull(actualCategories),
                () -> assertEquals(3, actualCategories.size()),
                () -> assertTrue(actualCategories.contains("Category 1")),
                () -> assertTrue(actualCategories.contains("Category 2")),
                () -> assertTrue(actualCategories.contains("Category 3")),

                () -> verify(productRepository, times(1)).findAllDistinctCategories()
        );
    }


    @Test
    @DisplayName("It should return brands list from Products list")
    void itShouldReturnBrandsList() {
        //Given
        List<String> brands = List.of("Brand 1", "Brand 2", "Brand 3");

        //Mocking
        when(productRepository.findAllDistinctBrands()).thenReturn(brands);

        List<String> actualBrands = underTest.getAllBrands();

        //Then
        assertAll(
                () -> assertNotNull(actualBrands),
                () -> assertEquals(3, actualBrands.size()),
                () -> assertTrue(actualBrands.contains("Brand 1")),
                () -> assertTrue(actualBrands.contains("Brand 2")),
                () -> assertTrue(actualBrands.contains("Brand 3")),

                () -> verify(productRepository, times(1)).findAllDistinctBrands()
        );
    }
}
