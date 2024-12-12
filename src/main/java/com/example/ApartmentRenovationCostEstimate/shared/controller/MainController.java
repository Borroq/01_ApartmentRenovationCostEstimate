package com.example.ApartmentRenovationCostEstimate.shared.controller;

import com.example.ApartmentRenovationCostEstimate.cart.CartService;
import com.example.ApartmentRenovationCostEstimate.cart.dtos.CartListDto;
import com.example.ApartmentRenovationCostEstimate.product.dtos.ProductResponseDto;
import com.example.ApartmentRenovationCostEstimate.product.ProductService;
import com.example.ApartmentRenovationCostEstimate.room.RoomService;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomResponseDto;
import com.example.ApartmentRenovationCostEstimate.shared.dtos.PageMetadata;
import com.example.ApartmentRenovationCostEstimate.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final String title = "ARCEA - Apartment Renovation Cost Estimate Application";
    private final UserService userService;
    private final ProductService productService;
    private final RoomService roomService;
    private final CartService cartService;

    public MainController(UserService userService, ProductService productService, RoomService roomService, CartService cartService) {
        this.userService = userService;
        this.productService = productService;
        this.roomService = roomService;
        this.cartService = cartService;
    }


    @GetMapping("/home")
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
    public String products (Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDto> productPage = productService.getAllProduct(pageable);

        model.addAttribute("title", title);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("pageMetadata", new PageMetadata(
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalPages(),
                productPage.getTotalElements()
        ));

        return "products";
    }


    @GetMapping("/rooms")
    public String rooms (Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RoomResponseDto> roomsPage = roomService.getAllRoom(pageable);

        model.addAttribute("title", title);
        model.addAttribute("rooms", roomsPage.getContent());
        model.addAttribute("pageMetadata", new PageMetadata(
                roomsPage.getNumber(),
                roomsPage.getSize(),
                roomsPage.getTotalPages(),
                roomsPage.getTotalElements()
        ));
        return "rooms";
    }


    @GetMapping("/carts")
    public String cart (Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CartListDto> cartsPage = cartService.getAllCarts(pageable);

        model.addAttribute("title", title);
        model.addAttribute("carts", cartsPage.getContent());
        model.addAttribute("pageMetadata", new PageMetadata(
                cartsPage.getNumber(),
                cartsPage.getSize(),
                cartsPage.getTotalPages(),
                cartsPage.getTotalElements()
        ));

        return "carts";
    }


    @GetMapping("/carts/cart-details/{cartId}")
    public String cartDetails (@PathVariable("cartId") Long cartId, Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);

        model.addAttribute("title", title);

        try {
            model.addAttribute("singleCart", cartService.getCartById(cartId, pageable));
            return "cartDetails";
        } catch (ResourceNotFoundException e) {
            return "redirect:/carts";
        }
    }


    @GetMapping("/login")
    public String login (Model model) {
        return "login";
    }

}
