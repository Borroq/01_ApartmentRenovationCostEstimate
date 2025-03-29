package com.example.ApartmentRenovationCostEstimate.cart;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    Page<CartItem> findPageByCartId(Long cartId, Pageable pageable);
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

}
