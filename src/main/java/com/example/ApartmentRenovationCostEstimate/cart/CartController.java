package com.example.ApartmentRenovationCostEstimate.cart;


import com.example.ApartmentRenovationCostEstimate.cart.dtos.AddProductRequest;
import com.example.ApartmentRenovationCostEstimate.cart.dtos.CartListDto;
import com.example.ApartmentRenovationCostEstimate.cart.dtos.CartResponseDto;
import com.example.ApartmentRenovationCostEstimate.cart.dtos.CreateCartDto;
import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;

    }


    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> createCart(@Valid @RequestBody CreateCartDto createCartDto){
        CartResponseDto saveCartDto = cartService.createCart(createCartDto.getUserId(), createCartDto.getName());

        ApiResponse<CartResponseDto> response = new ApiResponse<>("Cart created successfully.", saveCartDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("{cartId}/products")
    public ResponseEntity<ApiResponse<CartResponseDto>> addProductToCart(@Valid @PathVariable Long cartId, @RequestBody AddProductRequest request) {
            CartResponseDto addedProduct = cartService.addProductToCart(cartId, request);

            ApiResponse<CartResponseDto> response = new ApiResponse<>("Product added to cart", addedProduct);

            return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("{cartId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> getCartById(
            @PathVariable("cartId") Long cartId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        Pageable pageable = PageRequest.of(page, size);
        CartResponseDto cart = cartService.getCartById(cartId, pageable);

        return new ResponseEntity<>(new ApiResponse<>("Cart retrieved successfully",cart), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CartListDto>>> getAllCarts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CartListDto> allCarts = cartService.getAllCarts(pageable);

        return new ResponseEntity<>(new ApiResponse<>("Carts retrieved successfully", allCarts), HttpStatus.OK);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<ApiResponse<Page<CartListDto>>> getAllCartsByUserId(@PathVariable("userId") Long userId, Pageable pageable) {
        Page<CartListDto> allCarts = cartService.getAllCartsByUser(userId, pageable);

        return new ResponseEntity<>(new ApiResponse<>("Carts retrieved successfully", allCarts), HttpStatus.OK);
    }

    @DeleteMapping("{cartId}/products/{productId}")
    public ResponseEntity<ApiResponse> removeProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
            cartService.removeProductFromCart(cartId, productId);

            return new ResponseEntity<>(new ApiResponse<>("Product successfully removed"), HttpStatus.OK);
    }

    @DeleteMapping("{cartId}")
    public ResponseEntity<ApiResponse> deleteCart(@PathVariable("cartId") Long cartId) {
        cartService.deleteCart(cartId);

        return new ResponseEntity<>(new ApiResponse<>("Cart successfully deleted"), HttpStatus.OK);
    }

    @GetMapping("{cartId}/products/categories")
    public ResponseEntity<List<String>> getProductCategoriesFromCart(@PathVariable("cartId") Long cartId) {
        List<String> categories = cartService.getAllProductCategoriesFromCart(cartId);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("{cartId}/products/category/{category}")
    public ResponseEntity<List<CartItem>> getProductByCategoryFromCart(@PathVariable("cartId") Long cartId, @PathVariable("category") String category) {
        List<CartItem> productByCategory = cartService.getProductByCategoryFromCart(cartId, category);

        return new ResponseEntity<>(productByCategory, HttpStatus.OK);
    }

}
