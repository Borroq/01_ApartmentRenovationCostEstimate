package com.example.ApartmentRenovationCostEstimate.cart;


import com.example.ApartmentRenovationCostEstimate.cart.DTOs.AddProductRequest;
import com.example.ApartmentRenovationCostEstimate.cart.DTOs.CartListDto;
import com.example.ApartmentRenovationCostEstimate.cart.DTOs.CartResponseDto;
import com.example.ApartmentRenovationCostEstimate.cart.DTOs.CreateCartDTO;
import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorType;
import com.example.ApartmentRenovationCostEstimate.product.ProductService;
import com.example.ApartmentRenovationCostEstimate.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;


    @Autowired
    public CartController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createCart(@Valid @RequestBody CreateCartDTO createCartDTO){
        Cart saveCart = cartService.createCart(createCartDTO.getUserId(), createCartDTO.getName());

        return new ResponseEntity<>(new ApiResponse<>("Cart created successfully.", saveCart), HttpStatus.CREATED);
    }


    @PostMapping("{cartId}/products")
    public ResponseEntity<?> addProductToCart(@Valid @PathVariable Long cartId, @RequestBody AddProductRequest request) {
        /*Dodanie produktu do koszyka*/
        try {
            Cart addedProduct = cartService.addProductToCart(cartId, request);
            return ResponseEntity.ok(new ApiResponse<>("Product added to cart", addedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ErrorType.FAILED_TO_ADD_PRODUCT_TO_CART," An unexpected error occurred"));
        }
    }

    @GetMapping("{cartId}")
    public ResponseEntity<Object> getCartById(@PathVariable("cartId") Long cartId) {
        CartResponseDto cart = cartService.getCartById(cartId);

        return new ResponseEntity<>(new ApiResponse<>("Cart retrieved successfully",cart), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllCarts(Pageable pageable) {
        Page<CartListDto> allCarts = cartService.getAllCarts(pageable);
        if (allCarts.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>("No cart found."), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse<>("Carts retrieved successfully", allCarts), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getAllCartsByUserId(@PathVariable("userId") Long userId, Pageable pageable) {
        Page<CartListDto> allCarts = cartService.getAllCartsByUser(userId, pageable);
        if (allCarts.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>("No cart found."), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse<>("Carts retrieved successfully", allCarts), HttpStatus.OK);
    }

    @DeleteMapping("{cartId}/products/{productId}")
    public ResponseEntity<Object> removeProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
            cartService.removeProductFromCart(cartId, productId);

            return new ResponseEntity<>(new ApiResponse<>("Product successfully removed"), HttpStatus.OK);
    }

    @DeleteMapping("{cartId}")
    public ResponseEntity<Object> deleteCart(@PathVariable("cartId") Long cartId) {
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
