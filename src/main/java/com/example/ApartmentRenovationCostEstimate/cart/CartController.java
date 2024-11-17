package com.example.ApartmentRenovationCostEstimate.cart;


import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorResponse;
import com.example.ApartmentRenovationCostEstimate.product.Product;
import com.example.ApartmentRenovationCostEstimate.response.ErrorType;
import com.example.ApartmentRenovationCostEstimate.user.User;
import com.example.ApartmentRenovationCostEstimate.product.ProductService;
import com.example.ApartmentRenovationCostEstimate.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private CartService cartService;
    private UserService userService;
    private ProductService productService;


    @Autowired
    public CartController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createCart(@RequestBody Map<String, Object> payload){
        if (!payload.containsKey("userId") || payload.get("userId") == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.USERID_IS_MISSING), HttpStatus.BAD_REQUEST);
        }
        if (!payload.containsKey("name") || payload.get("name") == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.CART_NAME_IS_MISSING), HttpStatus.BAD_REQUEST);
        }

        Long userId;
        String name;

        try {
             userId = Long.valueOf(payload.get("userId").toString());
             name = payload.get("name").toString();
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.INVALID_USER_FORMAT), HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.USER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        Cart savedCart = cartService.createCart(user, name);
        return new ResponseEntity<>(new ApiResponse<>("Cart created successfully.", savedCart), HttpStatus.CREATED);
    }

    @PostMapping("{cartId}/products")
    public ResponseEntity<?> addProductToCart(@PathVariable Long cartId, @RequestBody AddProductRequest request) {

        /*Sprawdzanie czy koszyk instnieje*/
        Cart cart = cartService.getCartById(cartId);
        if(cart == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.CART_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        /*Sprawdzanie czy produkt instnieje*/
        Product product = productService.getProductById(request.getProductId());
        if(product == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        /*Dodanie produktu do koszyka*/
        try {
            Cart addedProduct = cartService.addProductToCart(cartId, request.getProductId(), request.getQuantity());
            return new ResponseEntity<>(new ApiResponse<>("Product added to cart", addedProduct), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.FAILED_TO_ADD_PRODUCT_TO_CART," An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{cartId}")
    public ResponseEntity<Object> getCartById(@PathVariable("cartId") Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        if(cart == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.CART_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse<>("Cart retrieved successfully",cart), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> allCarts = cartService.getAllCarts();
        return new ResponseEntity<>(allCarts, HttpStatus.OK);
    }

    @DeleteMapping("{cartId}/products/{productId}")
    public ResponseEntity<Object> removeProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
        try {
            cartService.removeProductFromCart(cartId, productId);
            return new ResponseEntity<>(new ApiResponse<>("Product successfully removed"), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.CART_OR_PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{cartId}")
    public ResponseEntity<Object> deleteCart(@PathVariable("cartId") Long cartId) {
        Optional<Cart> cart = cartService.findById(cartId);
        if (cart.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.CART_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        cartService.deleteCart(cartId);
        return new ResponseEntity<>(new ApiResponse<>("Cart successfully deleted"), HttpStatus.OK);
    }

    @GetMapping("{cartId}/products/categories")
    public ResponseEntity<List<String>> getProductCategoriesFromCart(@PathVariable("cartId") Long cartId) {
        List<String> categories = cartService.getAllProductCategoriesFromCart(cartId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

/*    @GetMapping("{cartId}/products/categories")
    public ResponseEntity<Object> getProductCategoriesFromCart(@PathVariable("cartId") Long cartId) {
        List<String> categories = cartService.getAllProductCategoriesFromCart(cartId);
        return new ResponseEntity<>(new ApiResponse<>("Product categories fetched successfully", categories), HttpStatus.OK);
    }*/

    @GetMapping("{cartId}/products/category/{category}")
    public ResponseEntity<List<CartItem>> getProductByCategoryFromCart(@PathVariable("cartId") Long cartId, @PathVariable("category") String category) {
        List<CartItem> productByCategory = cartService.getProductByCategoryFromCart(cartId, category);
        return new ResponseEntity<>(productByCategory, HttpStatus.OK);
    }

}
