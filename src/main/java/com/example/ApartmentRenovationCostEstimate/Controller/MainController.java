package com.example.ApartmentRenovationCostEstimate.Controller;

import com.example.ApartmentRenovationCostEstimate.UserService;
import com.example.ApartmentRenovationCostEstimate.entityDomain.User;
import com.example.ApartmentRenovationCostEstimate.repositories.ProductRepository;
import com.example.ApartmentRenovationCostEstimate.repositories.RoomRepository;
import com.example.ApartmentRenovationCostEstimate.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Controller
public class MainController{
    private final String title = "ARCEA - Apartment Renovation Cost Estimate Application";
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private ProductRepository productRepository;

    public MainController(UserRepository userRepository, RoomRepository roomRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home (Model model){
        model.addAttribute("title", title);
        return "home";
    }


}
