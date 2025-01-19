package com.example.ApartmentRenovationCostEstimate.product;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    void itShouldFindProductsByGivenCategory() {
        //Given
        String category = "Piekarniki";

        //When
        List<Product> testProduct = productRepository.findByCategory(category);

        //Then
        assertAll(
                () -> assertThat(testProduct.size()).isEqualTo(8),
                () -> assertThat(testProduct).isNotEmpty(),
                () -> testProduct.forEach(product -> assertThat(product.getCategory()).isEqualTo(category))
        );
    }


    @Test
    void itShouldReturnEmptyListWhenCategoryDoesNotExist() {
        //Given
        String category = "Non exist category";

        //When
        List<Product> testProduct = productRepository.findByCategory(category);

        //Then
        assertThat(testProduct).isEmpty();
    }


    @Test
    void itShouldFindAllDistinctProductCategories() {

        //When
        List<String> productCategories = productRepository.findAllDistinctCategories();

        //Then
        assertAll(
                () -> assertNotNull(productCategories, "Categories list should not be null"),
                () -> assertThat(productCategories).isNotEmpty()
        );
    }


    @Test
    void itShouldFindAllDistinctProductBrands() {

        //When
        List<String> productBrands = productRepository.findAllDistinctBrands();

        //Then
        assertAll(
                () -> assertNotNull(productBrands, "Brands list should not be null"),
                () -> assertThat(productBrands).isNotEmpty()
        );
    }
}
