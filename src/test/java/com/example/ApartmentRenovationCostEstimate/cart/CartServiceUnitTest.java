package com.example.ApartmentRenovationCostEstimate.cart;

import com.example.ApartmentRenovationCostEstimate.cart.dtos.AddProductRequest;
import com.example.ApartmentRenovationCostEstimate.cart.dtos.CartItemDto;
import com.example.ApartmentRenovationCostEstimate.cart.dtos.CartResponseDto;
import com.example.ApartmentRenovationCostEstimate.exceptions.cart.CartNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.product.ProductNotFoundException;
import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserNotFoundException;
import com.example.ApartmentRenovationCostEstimate.product.Product;
import com.example.ApartmentRenovationCostEstimate.product.ProductRepository;
import com.example.ApartmentRenovationCostEstimate.user.User;
import com.example.ApartmentRenovationCostEstimate.user.UserRepository;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserSummaryDto;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.PAGE_DEFAULT;
import static com.example.ApartmentRenovationCostEstimate.shared.PaginationConstant.SIZE_DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartServiceUnitTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;

    private CartServiceImpl underTest;
    private AutoCloseable autoCloseable;


    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CartServiceImpl(userRepository, cartRepository, cartItemRepository, productRepository, modelMapper);
    }

    @AfterEach
    void afterAll() throws Exception {
        autoCloseable.close();
    }


    @Test
    @DisplayName("It should save new cart")
    void itShouldSaveNewCart() {
        //Given
        Long userId = 1L;
        String cartName = "Test Cart";

        User user = new User();
        user.setId(userId);
        user.setName("Test User");

        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setName(cartName);
        newCart.setTotalCost(BigDecimal.ZERO);

        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setId(1L);
        cartResponseDto.setUser(new UserSummaryDto());
        cartResponseDto.setName(cartName);
        cartResponseDto.setTotalCost(BigDecimal.ZERO);

        //Mocking
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);
        when(modelMapper.map(newCart, CartResponseDto.class)).thenReturn(cartResponseDto);

        //When
        CartResponseDto actualResponse = underTest.createCart(userId, cartName);

        //Then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(cartResponseDto, actualResponse),

                () -> verify(userRepository, times(1)).findById(userId),
                () -> verify(cartRepository, times(1)).save(any(Cart.class)),
                () -> verify(modelMapper, times(1)).map(newCart, CartResponseDto.class)
        );
    }


    @Test
    @DisplayName("It should return UserNotFoundException when user does not exist when creating new Cart")
    void itShouldReturnExceptionWhenUserDoesNotExistWhenCreatingNewCart() {
        //Given
        Long userId = 1L;
        String cartName = "Test Cart";

        //Mocking
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> underTest.createCart(userId, cartName));

        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    @DisplayName("It should add product to cart")
    void itShouldAddProductToCart() {
        //Given
        Long cartId = 1L;
        Long productId = 10L;
        int quantity = 5;

        AddProductRequest addProductRequest = new AddProductRequest(productId, quantity);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setTotalCost(BigDecimal.ZERO);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal(100));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setId(cartId);
        cartResponseDto.setTotalCost(new BigDecimal(500));

        //Mocking
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(cart)).thenReturn(cart);
        when(modelMapper.map(cart, CartResponseDto.class)).thenReturn(cartResponseDto);

        //When

        CartResponseDto actualResponse = underTest.addProductToCart(cartId, addProductRequest);

        //Then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertThat(actualResponse.getId()).isEqualTo(cartId),
                () -> assertThat(actualResponse.getTotalCost()).isEqualTo(new BigDecimal(500)),

                () -> verify(cartRepository, times(1)).findById(cartId),
                () -> verify(productRepository, times(1)).findById(productId),
                () -> verify(cartItemRepository, times(1)).findByCartIdAndProductId(cartId, productId),
                () -> verify(cartItemRepository, times(1)).save(any(CartItem.class)),
                () -> verify(cartRepository, times(1)).save(cart),
                () -> verify(modelMapper, times(1)).map(cart, CartResponseDto.class)
        );
    }


    @Test
    @DisplayName("It should update product quantity in cart when product already exists")
    void itShouldUpdateProductQuantityInCartWhenProductAlreadyExists() {
        //Given
        Long cartId = 1L;
        Long productId = 10L;
        int quantityToAdd = 15;

        AddProductRequest addProductRequest = new AddProductRequest(productId, quantityToAdd);

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal(100));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(5);

        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setId(cartId);
        cartResponseDto.setTotalCost(new BigDecimal(1000));

        //Mocking
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(cart)).thenReturn(cart);
        when(modelMapper.map(cart, CartResponseDto.class)).thenReturn(cartResponseDto);

        //When

        CartResponseDto actualResponse = underTest.addProductToCart(cartId, addProductRequest);

        //Then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertThat(actualResponse.getId()).isEqualTo(cartId),
                () -> assertThat(actualResponse.getTotalCost()).isEqualTo(new BigDecimal(1000)),

                () -> verify(cartItemRepository, times(1)).save(any(CartItem.class)),
                () -> verify(cartRepository, times(1)).save(cart)
        );
    }


    @Test
    @DisplayName("It should throw cartNotFoundException when cart does not exist")
    void itShouldThrowCartNotFoundExceptionWhenCartDoesNotExist() {
        //Given
        Long cartId = 1L;
        AddProductRequest addProductRequest = new AddProductRequest(10L, 5);


        //Mocking
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(CartNotFoundException.class, () -> underTest.addProductToCart(cartId, addProductRequest));

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartItemRepository, times(0)).save(any(CartItem.class));
    }


    @Test
    @DisplayName("It should throw productNotFoundException when product does not exist")
    void itShouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        //Given
        Long cartId = 1L;
        Long productId = 10L;
        int quantity = 5;
        AddProductRequest addProductRequest = new AddProductRequest(productId, quantity);

        Cart cart = new Cart();
        cart.setId(cartId);

        //Mocking
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(ProductNotFoundException.class, () -> underTest.addProductToCart(cartId, addProductRequest));

        verify(cartRepository, times(1)).findById(cartId);
        verify(productRepository, times(1)).findById(productId);
        verify(cartItemRepository, times(0)).save(any(CartItem.class));
    }


    @Test
    @DisplayName("It should return cart by given Id if exists")
    void itShouldReturnCartByGivenId() {
        //Given
        Long cartId = 1L;
        Long productId = 10L;

        int page = Integer.parseInt(PAGE_DEFAULT);
        int size = Integer.parseInt(SIZE_DEFAULT);
        Pageable pageable = PageRequest.of(page, size);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setTotalCost(new BigDecimal("500.0"));

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setQuantity(5);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setQuantity(7);

        List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        Page<CartItem> cartItemPage = new  PageImpl<>(cartItems, pageable, 2);

        CartResponseDto expectedCartResponseDto = new CartResponseDto();
        expectedCartResponseDto.setId(cartId);
        expectedCartResponseDto.setTotalCost(new BigDecimal("500.0"));

        CartItemDto cartItemDto1 = new CartItemDto();
        cartItemDto1.setId(cartItem1.getId());
        cartItemDto1.setQuantity(cartItem1.getQuantity());

        CartItemDto cartItemDto2 = new CartItemDto();
        cartItemDto2.setId(cartItem2.getId());
        cartItemDto2.setQuantity(cartItem2.getQuantity());

        List<CartItemDto> cartItemDtos = List.of(cartItemDto1, cartItemDto2);

        //Mocking
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cartId, pageable)).thenReturn(cartItemPage);
        when(modelMapper.map(cart, CartResponseDto.class)).thenReturn(expectedCartResponseDto);
        when(modelMapper.map(cartItem1, CartItemDto.class)).thenReturn(cartItemDto1);
        when(modelMapper.map(cartItem2, CartItemDto.class)).thenReturn(cartItemDto2);

        //When
        CartResponseDto actualResponse = underTest.getCartById(cartId, pageable);

        //Then
        assertAll(
                () -> assertThat(actualResponse).isNotNull(),
                () -> assertThat(actualResponse.getId()).isEqualTo(cartId),
                () -> assertThat(actualResponse.getCartItems()).hasSize(2),
                () -> assertThat(actualResponse.getCartItems().get(0).getId()).isEqualTo(cartItemDto1.getId()),
                () -> assertThat(actualResponse.getCartItems().get(1).getId()).isEqualTo(cartItemDto2.getId()),

                () -> verify(cartRepository, times(1)).findById(cartId),
                () -> verify(cartItemRepository, times(1)).findByCartId(cartId, pageable),
                () -> verify(modelMapper, times(1)).map(cart, CartResponseDto.class),
                () -> verify(modelMapper, times(2)).map(any(CartItem.class), eq(CartItemDto.class))
        );

    }
}