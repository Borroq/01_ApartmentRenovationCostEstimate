package com.example.ApartmentRenovationCostEstimate.services;

import com.example.ApartmentRenovationCostEstimate.entity.Cart;
import com.example.ApartmentRenovationCostEstimate.entity.CartItem;
import com.example.ApartmentRenovationCostEstimate.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    Cart createCart(User user, String name);
    Cart addProductToCart(Long cartId, Long productId, int quantity);
    Cart getCartById(Long cartId);
    List<Cart> getAllCarts();
    void removeProductFromCart(Long cartId, Long productId);
    void deleteCart(Long cartId);
    BigDecimal calculateCartTotalCost(Long cardId);
    void updateTotalPrice(CartItem cartItem);
    List<String> getAllProductCategoriesFromCart(Long cartId);
    List<CartItem> getProductByCategoryFromCart(Long cartId, String category);
}
