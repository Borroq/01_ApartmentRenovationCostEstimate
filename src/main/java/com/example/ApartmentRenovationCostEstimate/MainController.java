package com.example.ApartmentRenovationCostEstimate;

import com.example.ApartmentRenovationCostEstimate.repositories.ProductRepository;
import com.example.ApartmentRenovationCostEstimate.repositories.RoomRepository;
import com.example.ApartmentRenovationCostEstimate.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private ProductRepository productRepository;

    public MainController(UserRepository userRepository, RoomRepository roomRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.productRepository = productRepository;
    }
}
