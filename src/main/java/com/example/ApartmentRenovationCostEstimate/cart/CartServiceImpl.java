package com.example.ApartmentRenovationCostEstimate.cart;

import com.example.ApartmentRenovationCostEstimate.cart.DTOs.AddProductRequest;
import com.example.ApartmentRenovationCostEstimate.cart.DTOs.CartItemDto;
import com.example.ApartmentRenovationCostEstimate.cart.DTOs.CartListDto;
import com.example.ApartmentRenovationCostEstimate.cart.DTOs.CartResponseDto;
import com.example.ApartmentRenovationCostEstimate.exceptions.cart.CartItemNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.cart.CartNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserNotFoundException;
import com.example.ApartmentRenovationCostEstimate.product.Product;
import com.example.ApartmentRenovationCostEstimate.shared.dtos.PageMetadata;
import com.example.ApartmentRenovationCostEstimate.user.DTOs.UserSummaryDto;
import com.example.ApartmentRenovationCostEstimate.user.User;
import com.example.ApartmentRenovationCostEstimate.product.ProductRepository;
import com.example.ApartmentRenovationCostEstimate.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(UserRepository userRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public Cart createCart(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cart = new Cart();

        cart.setUser(user);
        cart.setName(name);
        cart.setTotalCost(BigDecimal.ZERO);

        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart addProductToCart(Long cartId, AddProductRequest addProductRequest) {
        Cart cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        Product product = productRepository
                .findById(addProductRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cartId, addProductRequest.getProductId())
                .orElse(new CartItem());

        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(addProductRequest.getQuantity());

        //Update total price for this cart item
        updateTotalPrice(cartItem);
        cartItemRepository.save(cartItem);

        //Calculating the TOTAL COST of Cart
        BigDecimal newTotalCost = calculateCartTotalCost(cartId);
        cart.setTotalCost(newTotalCost);

        return cartRepository.save(cart);
    }


    @Override
    public CartResponseDto getCartById(Long cartId, Pageable pageable) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        Page<CartItem> cartItemPage = cartItemRepository.findByCartId(cartId, pageable);

        CartResponseDto cartResponseDto = modelMapper.map(cart, CartResponseDto.class);

        List<CartItemDto> cartItems = cartItemPage.getContent()
                .stream()
                .map(item -> modelMapper.map(item, CartItemDto.class))
                .collect(Collectors.toList());

        cartResponseDto.setCartItems(cartItems);
        cartResponseDto.setCartItemsPageMetadata(new PageMetadata(
                cartItemPage.getNumber(),
                cartItemPage.getSize(),
                cartItemPage.getTotalPages(),
                cartItemPage.getTotalElements()
        ));

        return cartResponseDto;
        //return modelMapper.map(cart, CartResponseDto.class);
    }


    @Override
    public Page<CartListDto> getAllCarts(Pageable pageable) {

        return cartRepository.findAll(pageable)
                .map(cart -> {
                    CartListDto dto = new CartListDto();
                    dto.setId(cart.getId());
                    dto.setName(cart.getName());
                    dto.setTotalCost(cart.getTotalCost());
                    dto.setCartItemsCount(cart.getCartItems().size());
                    UserSummaryDto userDto = modelMapper.map(cart.getUser(), UserSummaryDto.class);
                    dto.setUser(userDto);
                    return dto;
                });
    }

    @Override
    public Page<CartListDto> getAllCartsByUser(Long userId, Pageable pageable) {

        return cartRepository.findByUserId(userId, pageable)
                .map(cart -> {
                    CartListDto dto = new CartListDto();
                    dto.setId(cart.getId());
                    dto.setName(cart.getName());
                    dto.setTotalCost(cart.getTotalCost());
                    dto.setCartItemsCount(cart.getCartItems().size());
                    UserSummaryDto userDto = modelMapper.map(cart.getUser(), UserSummaryDto.class);
                    dto.setUser(userDto);
                    return dto;
                });
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem not found"));

        cartItemRepository.delete(cartItem);

        BigDecimal newTotalCost = calculateCartTotalCost(cartId);
        cart.setTotalCost(newTotalCost);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

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
