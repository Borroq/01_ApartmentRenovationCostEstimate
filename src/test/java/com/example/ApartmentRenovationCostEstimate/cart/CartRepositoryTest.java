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


import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.PAGE_DEFAULT;
import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.SIZE_DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;


    @Test
    @DisplayName("It should find all carts by given user ID")
    void itShouldFindAllCartsByGivenUserId() {
        //Given
        Long userId = 13L;
        int page = Integer.parseInt(PAGE_DEFAULT);
        int size = Integer.parseInt(SIZE_DEFAULT);
        Pageable pageable = PageRequest.of(page, size);

        //When
        Page<Cart> testCarts = cartRepository.findByUserId(userId, pageable);

        //Then
        assertAll(
                () -> assertThat(testCarts.getContent().size()).isEqualTo(2),
                () -> assertThat(testCarts.getSize()).isEqualTo(10),
                () -> assertThat(testCarts).isNotEmpty(),
                () -> assertThat(testCarts.getContent()).allMatch(cart -> cart.getUser().getId().equals(userId))
        );
    }


    @Test
    @DisplayName("It should return empty list when User has no carts")
    void itShouldReturnEmptyListWhenUserHasNoCarts() {
        //Given
        Long userId = 16L;
        int page = Integer.parseInt(PAGE_DEFAULT);
        int size = Integer.parseInt(SIZE_DEFAULT);
        Pageable pageable = PageRequest.of(page, size);

        //When
        Page<Cart> testCarts = cartRepository.findByUserId(userId, pageable);

        //Then
        assertThat(testCarts.getContent()).isEmpty();
    }


}
