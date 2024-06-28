package com.example.ApartmentRenovationCostEstimate.controller;

import com.example.ApartmentRenovationCostEstimate.repositories.ProductRepository;
import com.example.ApartmentRenovationCostEstimate.repositories.RoomRepository;
import com.example.ApartmentRenovationCostEstimate.repositories.UserRepository;
import com.example.ApartmentRenovationCostEstimate.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final String title = "ARCEA - Apartment Renovation Cost Estimate Application";
    private final UserService userService;
   /* private UserRepository userRepository;
    private RoomRepository roomRepository;
    private ProductRepository productRepository;*/

    public MainController(UserService userService/*, UserRepository userRepository, RoomRepository roomRepository, ProductRepository productRepository*/) {
        this.userService = userService;
/*        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.productRepository = productRepository;*/
    }



    @GetMapping("/")
    public String home (Model model) {
        model.addAttribute("title", title);
        model.addAttribute("users", userService.getAllUsers());
        return "home";
    }




}
