package com.example.ApartmentRenovationCostEstimate.controller;

import com.example.ApartmentRenovationCostEstimate.dto.AddProductRequest;
import com.example.ApartmentRenovationCostEstimate.entity.Cart;
import com.example.ApartmentRenovationCostEstimate.entity.Product;
import com.example.ApartmentRenovationCostEstimate.entity.User;
import com.example.ApartmentRenovationCostEstimate.services.CartService;
import com.example.ApartmentRenovationCostEstimate.services.ProductService;
import com.example.ApartmentRenovationCostEstimate.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
            return new ResponseEntity<>("userId is missing", HttpStatus.BAD_REQUEST);
        }
        if (!payload.containsKey("name") || payload.get("name") == null) {
            return new ResponseEntity<>("name is missing", HttpStatus.BAD_REQUEST);
        }
        Long userId;
        String name;
        try {
             userId = Long.valueOf(payload.get("userId").toString());
             name = payload.get("name").toString();
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid userId format", HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        Cart savedCart = cartService.createCart(user, name);
        return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
    }

    @PostMapping("{cartId}/products")
    public ResponseEntity<?> addProductToCart(@PathVariable Long cartId, @RequestBody AddProductRequest request) {
        Cart cart = cartService.getCartById(cartId);

        /*Sprawdzanie czy koszyk instnieje*/
        if(cart == null) {
            return  new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        }

        /*Sprawdzanie czy produkt instnieje*/
        Product product = productService.getProductById(request.getProductId());
        if(product == null) {
            return  new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

        /*Dodanie produktu do koszyka*/
        try {
            cartService.addProductToCart(cartId, request.getProductId(), request.getQuantity());
            return new ResponseEntity<>("Product added to cart", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add product to cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable("cartId") Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> allCarts = cartService.getAllCarts();
        return new ResponseEntity<>(allCarts, HttpStatus.OK);
    }

    @DeleteMapping("{cartId}/products/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return new ResponseEntity<>("Product sucessfully removed", HttpStatus.OK);
    }

    @GetMapping("{cartId}/cart-total-cost")
    public ResponseEntity<BigDecimal> calculateTotalCost(@PathVariable Long cartId) {
        BigDecimal totalCostCart = cartService.calculateTotalCost(cartId);
        if (totalCostCart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(totalCostCart, HttpStatus.OK);
    }
}
