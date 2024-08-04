package com.example.ApartmentRenovationCostEstimate.services;

import com.example.ApartmentRenovationCostEstimate.entity.Cart;
import com.example.ApartmentRenovationCostEstimate.entity.CartItem;
import com.example.ApartmentRenovationCostEstimate.entity.Product;
import com.example.ApartmentRenovationCostEstimate.entity.User;
import com.example.ApartmentRenovationCostEstimate.repositories.CartItemRepository;
import com.example.ApartmentRenovationCostEstimate.repositories.CartRepository;
import com.example.ApartmentRenovationCostEstimate.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CartServiceImplement implements CartService{
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;

    @Autowired
    public CartServiceImplement(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }


    @Override
    public Cart createCart(User user, String name) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setName(name);
        return cartRepository.save(cart);
    }

    @Override
    public Cart addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId).orElse(new CartItem());

        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);
        return cart;
    }

    @Override
    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Override
    public List<Cart> getAllCarts() {
        Iterable<Cart> carts = cartRepository.findAll();
        return StreamSupport.stream(carts.spliterator(),false)
                .collect(Collectors.toList());
    }

    @Override
    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public BigDecimal calculateTotalCost(Long cardId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cardId);
        return cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
