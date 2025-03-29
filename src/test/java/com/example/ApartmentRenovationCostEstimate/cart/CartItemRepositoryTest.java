package com.example.ApartmentRenovationCostEstimate.cart;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.PAGE_DEFAULT;
import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.SIZE_DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;


    @Test
    @DisplayName("It should find all cart items by given cart ID")
    void itShouldFindAllCartsItemsByGivenCartId() {
        //Given
        Long cartId = 4L;

        //When
        List<CartItem> testCartItems = cartItemRepository.findByCartId(cartId);

        //Then
        assertAll(
                () -> assertThat(testCartItems.size()).isEqualTo(58),
                () -> testCartItems.forEach(cartItem -> assertThat(cartItem.getCart().getId()).isEqualTo(cartId)),
                () -> testCartItems.forEach(cartItem -> assertThat(cartItem.getProduct()).isNotNull())
        );
    }


    @Test
    @DisplayName("It should return empty list if no cart items are gound gor given cart ID")
    void ItShouldReturnEmptyListIfNoCartItemsAreFound() {
        //Given
        Long cartId = 11L;

        //When
        List<CartItem> testCartItems = cartItemRepository.findByCartId(cartId);

        //Then
        assertThat(testCartItems).isEmpty();
    }


    @Test
    @DisplayName("It should find all cart items by given cart ID - with Pagination")
    void itShouldFindAllCartsItemsByGivenCartIdWithPagination() {
        //Given
        Long cartId = 4L;
        int page = Integer.parseInt(PAGE_DEFAULT);
        int size = Integer.parseInt(SIZE_DEFAULT);

        Pageable pageable = PageRequest.of(page, size);

        //When
        Page<CartItem> testCartItems = cartItemRepository.findPageByCartId(cartId, pageable);

        //Then
        assertAll(
                () -> assertThat(testCartItems.getContent().size()).isEqualTo(10),
                () -> assertThat(testCartItems.getTotalPages()).isEqualTo(6),
                () -> testCartItems.forEach(cartItem -> assertThat(cartItem.getCart().getId()).isEqualTo(cartId)),
                () -> testCartItems.forEach(cartItem -> assertThat(cartItem.getProduct()).isNotNull())
        );
    }


    @Test
    @DisplayName("It should find all cart items by given cart ID and Product ID")
    void itShouldFindAllCartsItemsByGivenCartIdAndProductId() {
        //Given
        Long cartId = 4L;
        Long productId = 20L;

        //When
        Optional<CartItem> testCartItems = cartItemRepository.findByCartIdAndProductId(cartId, productId);

        //Then
        assertThat(testCartItems).isPresent().hasValueSatisfying(
                cartItem -> {
                    assertThat(cartItem.getProduct().getBrand()).isEqualTo("Knauf");
                    assertThat(cartItem.getProduct().getId()).isEqualTo(productId);
                    assertThat(cartItem.getQuantity()).isEqualTo(4);
                    assertThat(cartItem.getTotalPrice()).isEqualTo(new BigDecimal("548.00"));
                }
        );
    }

}
