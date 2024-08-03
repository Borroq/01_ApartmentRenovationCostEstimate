package com.example.ApartmentRenovationCostEstimate.controller;

import com.example.ApartmentRenovationCostEstimate.entity.Cart;
import com.example.ApartmentRenovationCostEstimate.entity.User;
import com.example.ApartmentRenovationCostEstimate.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private CartService cartService;
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(@RequestBody User user){
        Cart savedCart = cartService.createCart(user);
        return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
    }

    @PostMapping("{cartId}/products")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cardId, @RequestParam Integer productId, @RequestParam int quantity) {
        Cart saveProduct = cartService.addProductToCart(cardId, productId, quantity);
        return new ResponseEntity<>(saveProduct, HttpStatus.CREATED);
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
