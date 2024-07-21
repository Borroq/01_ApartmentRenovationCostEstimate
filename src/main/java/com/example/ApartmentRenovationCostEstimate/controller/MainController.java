package com.example.ApartmentRenovationCostEstimate.controller;

import com.example.ApartmentRenovationCostEstimate.services.ProductService;
import com.example.ApartmentRenovationCostEstimate.services.RoomService;
import com.example.ApartmentRenovationCostEstimate.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final String title = "ARCEA - Apartment Renovation Cost Estimate Application";
    private final UserService userService;
    private final ProductService productService;
    private final RoomService roomService;

    public MainController(UserService userService, ProductService productService, RoomService roomService) {
        this.userService = userService;
        this.productService = productService;
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String home (Model model) {
        model.addAttribute("title", title);
        //model.addAttribute("users", userService.getAllUsers());
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
}
