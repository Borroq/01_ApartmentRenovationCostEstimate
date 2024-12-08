package com.example.ApartmentRenovationCostEstimate.cart;


import com.example.ApartmentRenovationCostEstimate.cart.dtos.AddProductRequest;
import com.example.ApartmentRenovationCostEstimate.cart.dtos.CartListDto;
import com.example.ApartmentRenovationCostEstimate.cart.dtos.CartResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;


public interface CartService {

    Cart createCart(Long userId, String name);
    Cart addProductToCart(Long cartId, AddProductRequest addProductRequest);
    CartResponseDto getCartById(Long cartId, Pageable pageable);
    Page<CartListDto> getAllCarts(Pageable pageable);
    Page<CartListDto> getAllCartsByUser(Long userId, Pageable pageable);
    void removeProductFromCart(Long cartId, Long productId);
    void deleteCart(Long cartId);
    BigDecimal calculateCartTotalCost(Long cardId);
    void updateTotalPrice(CartItem cartItem);
    List<String> getAllProductCategoriesFromCart(Long cartId);
    List<CartItem> getProductByCategoryFromCart(Long cartId, String category);
}
