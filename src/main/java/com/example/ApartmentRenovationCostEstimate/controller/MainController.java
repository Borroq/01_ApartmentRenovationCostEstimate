package com.example.ApartmentRenovationCostEstimate.controller;

import com.example.ApartmentRenovationCostEstimate.cart.CartService;
import com.example.ApartmentRenovationCostEstimate.database.DatabaseService;
import com.example.ApartmentRenovationCostEstimate.product.ProductService;
import com.example.ApartmentRenovationCostEstimate.room.RoomService;
import com.example.ApartmentRenovationCostEstimate.user.UserService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {
    private final String title = "ARCEA - Apartment Renovation Cost Estimate Application";
    private final UserService userService;
    private final ProductService productService;
    private final RoomService roomService;
    private final CartService cartService;
    private final DatabaseService databaseService;

    public MainController(UserService userService, ProductService productService, RoomService roomService, CartService cartService, DatabaseService databaseService) {
        this.userService = userService;
        this.productService = productService;
        this.roomService = roomService;
        this.cartService = cartService;
        this.databaseService = databaseService;
    }

    @GetMapping("/")
    public String home (Model model) {
        model.addAttribute("title", title);
        return "home";
    }

    @GetMapping("/users")
    public String users (Model model) {
        model.addAttribute("title", title);
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/products")
    public String products (Model model) {
        model.addAttribute("title", title);
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    @GetMapping("/rooms")
    public String rooms (Model model) {
        model.addAttribute("title", title);
        model.addAttribute("rooms", roomService.getAllRoom());
        return "rooms";
    }
    @GetMapping("/carts")
    public String cart (Model model) {
        model.addAttribute("title", title);
        model.addAttribute("carts", cartService.getAllCarts());
        return "carts";
    }
    @GetMapping("/carts/cart-details/{cartId}")
    public String cartDetails (@PathVariable("cartId") Long cartId, Model model) {
        model.addAttribute("title", title);
        try {
            model.addAttribute("singleCart", cartService.getCartById(cartId));
            return "cartDetails";
        } catch (ResourceNotFoundException e) {
            return "redirect:/carts";
        }
    }

}
