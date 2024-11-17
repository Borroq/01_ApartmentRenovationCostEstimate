package com.example.ApartmentRenovationCostEstimate.cart;

import com.example.ApartmentRenovationCostEstimate.product.Product;
import com.example.ApartmentRenovationCostEstimate.user.User;
import com.example.ApartmentRenovationCostEstimate.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }


    @Override
    public Cart createCart(User user, String name) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setName(name);
        cart.setTotalCost(BigDecimal.ZERO);
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

        //Update total price for this cart item
        updateTotalPrice(cartItem);
        cartItemRepository.save(cartItem);

        //Calculating the TOTAL COST of Cart
        BigDecimal newTotalCost = calculateCartTotalCost(cartId);
        cart.setTotalCost(newTotalCost);

        cartRepository.save(cart);

        return cart;
    }

    @Override
    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);
        //return cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
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

        BigDecimal newTotalCost = calculateCartTotalCost(cartId);
        cart.setTotalCost(newTotalCost);

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }


    @Override
    public BigDecimal calculateCartTotalCost(Long cardId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cardId);
        return cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public void updateTotalPrice(CartItem cartItem) {
        BigDecimal totalPrice = cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
        cartItem.setTotalPrice(totalPrice);
    }

    @Override
    public List<String> getAllProductCategoriesFromCart(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        return cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getCategory())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItem> getProductByCategoryFromCart(Long cartId, String category) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        List<CartItem> filteredItemsByCart = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getCategory().equals(category))
                .collect(Collectors.toList());

        if(filteredItemsByCart.isEmpty()) {
            throw new ResourceNotFoundException("No products found in the category: " + category);
        }

        return filteredItemsByCart;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }
}
